package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.client.users.UserClient;
import it.spindox.stagelab.magazzino.dto.WebClient.UserResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import it.spindox.stagelab.magazzino.repositories.FatturaWorkExecutionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecution;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecutionErrorType;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import it.spindox.stagelab.magazzino.entities.FatturaWorkExecution;






@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;
    private final FatturaMapper fatturaMapper;
    private final FatturaWorkExecutionRepository fatturaWorkExecutionRepository;
    @Getter
    private final UserClient userClient; // userclient

    // configuration properties
    @Value("${fatture.default-username:system}")
    private String defaultUsername;

    @PersistenceContext
    private EntityManager entityManager;

    // Helpers HH

    private String resolveUsername(String maybeUsername) {
        return (maybeUsername != null && !maybeUsername.isBlank())
                ? maybeUsername
                : defaultUsername;
    }

    private SXFatturaStatus determine(BigDecimal importo, BigDecimal pagato, LocalDate dataScadenza) {
        // Implementazione
        if (importo != null && pagato != null && pagato.compareTo(importo) >= 0) {
            return SXFatturaStatus.PAGATA;
        }
        if (dataScadenza != null && LocalDate.now().isAfter(dataScadenza)) {
            return SXFatturaStatus.SCADUTA;
        }
        return SXFatturaStatus.EMESSA;
    }


    // SEARCH

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> search(FatturaSearchRequest request) {

        if (request == null) return Page.empty();

        int page = Math.max(request.getPage(), 0);
        int size = Math.max(request.getSize(), 1);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Probe
        Fattura probe = new Fattura();

        if (request.getNumero() != null && !request.getNumero().isBlank()) {
            probe.setNumero(request.getNumero());
        }

        if (request.getIdProdotto() != null) {
            Prodotto p = new Prodotto();
            p.setId(request.getIdProdotto());
            probe.setProdotto(p);
        }

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            probe.setUsername(request.getUsername());
        }

        // Matcher
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withMatcher("numero", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact());

        // Query
        Page<Fattura> basePage =
                fatturaRepository.findAll(Example.of(probe, matcher), pageable);

        // Filtri extra in memoria
        List<Fattura> filtered = basePage.getContent().stream()

                // DATA FROM
                .filter(f -> request.getDataFrom() == null ||
                        (f.getDataFattura() != null &&
                                !f.getDataFattura().isBefore(request.getDataFrom())))

                // DATA TO
                .filter(f -> request.getDataTo() == null ||
                        (f.getDataFattura() != null &&
                                !f.getDataFattura().isAfter(request.getDataTo())))

                // IMPORTO MIN
                .filter(f -> request.getImportoMin() == null ||
                        (f.getImporto() != null &&
                                f.getImporto().compareTo(request.getImportoMin()) >= 0))

                // IMPORTO MAX
                .filter(f -> request.getImportoMax() == null ||
                        (f.getImporto() != null &&
                                f.getImporto().compareTo(request.getImportoMax()) <= 0))

                .toList();

        return new PageImpl<>(
                filtered.stream().map(fatturaMapper::toResponse).toList(),
                pageable,
                filtered.size()
        );
    }

    private static @NonNull Fattura getFattura(FatturaSearchRequest request) {
        Fattura probe = new Fattura();

        // Filtro per numero
        if (request.getNumero() != null && !request.getNumero().isBlank()) {
            probe.setNumero(request.getNumero());
        }

        // Filtro per prodotto
        if (request.getIdProdotto() != null) {
            Prodotto p = new Prodotto();
            p.setId(request.getIdProdotto());
            probe.setProdotto(p);
        }

        // Filtro per username
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            probe.setUsername(request.getUsername());
        }
        return probe;
    }

    // SEARCH SOLO ID

    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(FatturaSearchRequest req) {
        Page<FatturaResponse> pageResp = search(req);
        return pageResp.map(FatturaResponse::getId);
    }


    // GET ALL PAGED

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return fatturaRepository.findAll(pageable).map(fatturaMapper::toResponse);
    }

    @Override
    public Page<FatturaResponse> getByStatus(SXFatturaStatus status) {
        return null;
    }


    // GET BY STATUS

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> getByStatus(SXFatturaStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by("id").ascending()
        );
        Page<Fattura> result = fatturaRepository.findAllByStatus(status, pageable);
        return result.map(fatturaMapper::toResponse);
    }


    // MOCK HELPERS

    @Override
    public FatturaResponse createMockEmessa(Long idProdotto) {
        return createMockEmessa(new FatturaRequest(
                LocalDate.now(),
                idProdotto,
                1,
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(30)
        ));
    }

    @Override
    public FatturaResponse createMockScaduta(Long idProdotto) {
        return createMockScaduta(new FatturaRequest(
                LocalDate.now(),
                idProdotto,
                1,
                new BigDecimal("1.00"),
                LocalDate.now().minusDays(30)
        ));
    }

    @Override
    public FatturaResponse createMockPagata(Long idProdotto) {
        return createMockPagata(new FatturaRequest(
                LocalDate.now(),
                idProdotto,
                1,
                new BigDecimal("1.00"),
                LocalDate.now().plusDays(30)
        ));
    }


    // FATTURE TEST (EMESSA / SCADUTA / PAGATA)

    @Override
    public FatturaResponse createMockEmessa(FatturaRequest req) {

        Fattura f = new Fattura();
        f.setNumero("FAT-" + System.currentTimeMillis());
        f.setDataFattura(req.getDataFattura());
        f.setDataScadenza(req.getDataScadenza());
        f.setQuantita(req.getQuantita());
        f.setImporto(req.getImporto());
        f.setPagato(BigDecimal.ZERO);
        f.setStatus(SXFatturaStatus.EMESSA);

        f.setProdotto(prodottoRepository.findById(req.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato")));

        // usa default da properties
        f.setUsername(resolveUsername(req.getUsername()));

        fatturaRepository.save(f);

        FatturaWorkExecution exec = new FatturaWorkExecution();
        exec.setFatturaId(f.getId());
        exec.setStatus(SXFatturaJobexecution.SUCCESS);
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        FatturaResponse res = fatturaMapper.toResponse(f);
        res.setWorkExecutionId(exec.getId());
        return res;
    }

    @Override
    public FatturaResponse createMockScaduta(FatturaRequest req) {

        Fattura f = new Fattura();
        f.setNumero("FAT-" + System.currentTimeMillis());
        f.setDataFattura(req.getDataFattura());
        f.setDataScadenza(req.getDataScadenza());
        f.setQuantita(req.getQuantita());
        f.setImporto(req.getImporto());
        f.setPagato(BigDecimal.ZERO);
        f.setStatus(SXFatturaStatus.SCADUTA);

        f.setProdotto(prodottoRepository.findById(req.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato")));

        // usa default da properties
        f.setUsername(resolveUsername(req.getUsername()));

        fatturaRepository.save(f);

        FatturaWorkExecution exec = new FatturaWorkExecution();
        exec.setFatturaId(f.getId());
        exec.setStatus(SXFatturaJobexecution.SUCCESS);
        exec.setErrorType(SXFatturaJobexecutionErrorType.BUSINESS_WARNING);
        exec.setErrorMessage("Fattura non saldata entro la data di scadenza (test)");
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        FatturaResponse res = fatturaMapper.toResponse(f);
        res.setWorkExecutionId(exec.getId());
        return res;
    }

    @Override
    public FatturaResponse createMockPagata(FatturaRequest req) {

        Fattura f = new Fattura();
        f.setNumero("FAT-" + System.currentTimeMillis());
        f.setDataFattura(req.getDataFattura());
        f.setDataScadenza(req.getDataScadenza());
        f.setQuantita(req.getQuantita());
        f.setImporto(req.getImporto());
        f.setPagato(req.getImporto());
        f.setStatus(SXFatturaStatus.PAGATA);

        f.setProdotto(prodottoRepository.findById(req.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato")));

        // usa default da properties
        f.setUsername(resolveUsername(req.getUsername()));

        fatturaRepository.save(f);

        FatturaWorkExecution exec = new FatturaWorkExecution();
        exec.setFatturaId(f.getId());
        exec.setStatus(SXFatturaJobexecution.SUCCESS);
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        FatturaResponse res = fatturaMapper.toResponse(f);
        res.setWorkExecutionId(exec.getId());
        return res;
    }


    // CRUD

    @Override
    public FatturaResponse create(FatturaRequest request) {

        if (request == null)
            throw new IllegalArgumentException("FatturaRequest è null");

        if (request.getIdProdotto() == null)
            throw new InvalidFatturaException(null, request.getImporto(), request.getQuantita());

        if (request.getDataScadenza() != null &&
                request.getDataFattura() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {
            throw new InvalidFatturaException((Long) null);
        }

        Prodotto prodotto = prodottoRepository
                .findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        // CREA ENTITY DALLA REQUEST
        Fattura entity = fatturaMapper.toEntity(request, prodotto);

        // fallback dello username via property
        entity.setUsername(resolveUsername(entity.getUsername()));

        // GENERA NUMERO FATTURA
        Long seq = fatturaRepository.nextNumeroSeq();
        entity.setNumero("FAT-" + seq);

        // IMPOSTAZIONI DI DEFAULT
        entity.setPagato(BigDecimal.ZERO);
        entity.setStatus(determine(
                entity.getImporto(),
                entity.getPagato(),
                entity.getDataScadenza()
        ));

        entity = fatturaRepository.save(entity);
        return fatturaMapper.toResponse(entity);
    }

    @Override
    public FatturaResponse update(Long id, FatturaRequest request) {

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        if (entity.getStatus() == SXFatturaStatus.PAGATA)
            throw new InvalidFatturaException("La fattura è già pagata e non può essere modificata");

        if (request.getDataFattura() != null &&
                request.getDataScadenza() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {
            throw new InvalidFatturaException(id);
        }

        Prodotto prodotto = null;
        if (request.getIdProdotto() != null) {
            prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        }

        fatturaMapper.updateEntity(entity, request, prodotto);

        entity.setStatus(determine(
                entity.getImporto(),
                entity.getPagato(),
                entity.getDataScadenza()
        ));

        entity = fatturaRepository.save(entity);
        return fatturaMapper.toResponse(entity);
    }


    // GET BY ID : USERNAME

    @Override
    @Transactional(readOnly = true)

    public FatturaResponse getById(Long id) {

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        // mappa la fattura in response

        FatturaResponse response = fatturaMapper.toResponse(entity);

        // se esiste un username :  prova a recuperare i dati dal servizio utenti

        if (entity.getUsername() != null && !entity.getUsername().isBlank()) {

            UserResponse user = userClient.getUserByUsername(entity.getUsername());


            if (user != null) {
                response.setUserDetails(user); // aggiunge i dati dell' utente

            } else {
                response.setUserDetails(null);  //  explicit null
            }
        }

        log.info("DEBUG - Username fattura {} = '{}'", id, entity.getUsername());
        UserResponse user = null;

        try {
            user = userClient.getUserByUsername(entity.getUsername());

        } catch (Exception e) {
            log.error("Errore nella chiamata WebClient in getById", e);
        }

        log.info("DEBUG - Risultato WebClient = {}", user);

        return response;
    }

// GET BY ID NAME

    @Transactional(readOnly = true)
    public FatturaResponse getByIdByName(Long id) {

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        FatturaResponse response = fatturaMapper.toResponse(entity);

        if (entity.getUsername() != null && !entity.getUsername().isBlank()) {

            try {
                // RICERCA PER NAME

                UserResponse user = userClient.getUserByName(entity.getUsername());
                response.setUserDetails(user); // può essere null
            } catch (Exception e) {
                log.error("Errore WebClient [NAME] in getByIdByName", e);
                response.setUserDetails(null);
            }
        }

        return response;
    }















    // GET BY PRODOTTO

    @Override
    @Transactional(readOnly = true)
    public PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {

        if (idProdotto == null)
            return new PageImpl<>(List.of(), PageRequest.of(page, size), 0);

        List<Fattura> list = fatturaRepository.findByProdottoId(idProdotto);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<FatturaResponse> content =
                list.subList(start, end).stream()
                        .map(fatturaMapper::toResponse)
                        .toList();

        return new PageImpl<>(content, pageable, list.size());
    }


    // DELETE

    @Override
    public void delete(Long id) {

        if (id == null) return;

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata: id=" + id));

        fatturaRepository.delete(entity);
    }

}