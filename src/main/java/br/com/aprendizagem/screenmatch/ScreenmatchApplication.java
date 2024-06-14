package br.com.aprendizagem.screenmatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.aprendizagem.screenmatch.gui.MenuPrincipal;
import br.com.aprendizagem.screenmatch.repository.SerieRepository;
import br.com.aprendizagem.screenmatch.treatments.Clear;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repositorio;
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Clear.limpeza();
		MenuPrincipal menuPrincipal = new MenuPrincipal(repositorio);
		menuPrincipal.exibeMenu();
	}
}
