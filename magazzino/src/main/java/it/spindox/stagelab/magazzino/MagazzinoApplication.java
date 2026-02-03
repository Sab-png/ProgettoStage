
package it.spindox.stagelab.magazzino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MagazzinoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagazzinoApplication.class, args);
		System.out.println("Magazzino is Running");
	}
}
