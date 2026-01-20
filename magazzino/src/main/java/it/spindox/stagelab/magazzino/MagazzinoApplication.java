
package it.spindox.stagelab.magazzino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "it.spindox.stagelab")
public class MagazzinoApplication {

	static void main(String[] args) {
		SpringApplication.run(MagazzinoApplication.class, args);
	}
}
