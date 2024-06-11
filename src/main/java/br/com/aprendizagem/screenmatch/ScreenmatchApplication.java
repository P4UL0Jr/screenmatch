package br.com.aprendizagem.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.aprendizagem.screenmatch.gui.MenuPrincipal;
import br.com.aprendizagem.screenmatch.treatments.Clear;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
//my api key 948861192
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Clear.limpeza();
		MenuPrincipal menuPrincipal = new MenuPrincipal();
		menuPrincipal.main();
	}
}
