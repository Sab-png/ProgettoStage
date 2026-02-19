
package it.spindox.stagelab.magazzino;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.logging.Logger;


@EnableScheduling
@SpringBootApplication

public class MagazzinoApplication {

	private static final Logger logger = Logger.getLogger(MagazzinoApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(MagazzinoApplication.class, args);
		logger.info("Magazzino is Running");
	}
}
