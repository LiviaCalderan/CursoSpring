package br.com.alura.screenMatch.principal;

import br.com.alura.screenMatch.service.ConsumoApi;

import java.util.Scanner;

public class Principal {
    public void exibeMenu(){

        private Scanner leitura = new Scanner(System.in);
        private ConsumoApi consumo = new ConsumoApi();

        private final String ENDERECO = "https://www.omdbapi.com/?t=";
        private final String API_KEY = "&apikey=546e4c2f";

        System.out.println("Digite o nome da serie: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi
                .obterDados(ENDERECO + nomeSerie.replaceFirst(" ", "+") + API_KEY);

    }
}
