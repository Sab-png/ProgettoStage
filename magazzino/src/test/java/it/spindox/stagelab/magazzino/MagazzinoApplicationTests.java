package it.spindox.stagelab.magazzino;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class MagazzinoApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(MagazzinoApplication.class, args);
		System.out.println("Magazzino Test is Running");
	}
}
