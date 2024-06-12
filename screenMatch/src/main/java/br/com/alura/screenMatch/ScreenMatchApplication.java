package br.com.alura.screenMatch;

import br.com.alura.screenMatch.model.DadosSerie;
import br.com.alura.screenMatch.service.ConsumoApi;
import br.com.alura.screenMatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception { //funciona como main
		var consumoApi = new ConsumoApi();
		var json = consumoApi
				.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=546e4c2f");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}

}