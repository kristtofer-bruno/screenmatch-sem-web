package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.service.ConsumoAPI;

import java.util.Scanner;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=1d4a89c5";

    public void exibeMenu(){
        System.out.println("Digite o nome da séria para a busca: ");
        var nomeSerie = sc.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        //https://www.omdbapi.com/?t=+ nomeSerie + &season=" + numeroEpisodio + "&apikey=1d4a89c5
    }
}
