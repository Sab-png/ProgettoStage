package it.spindox.stagelab.magazzino;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class MagazzinoApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);  // semplice verifica: il contesto Spring parte correttamente
	}
}