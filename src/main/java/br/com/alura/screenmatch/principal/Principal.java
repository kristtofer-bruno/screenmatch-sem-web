package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=1d4a89c5";


    public void exibeMenu(){
        System.out.println("Digite o nome da série para a busca: ");
        var nomeSerie = sc.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for(int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);
        // :: = o mesmo que temporadas.forEach(t -> System.out.println(t));

//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //lambda -> funções que terão um parametro utilizando uma única letra para declarar o parametro
        // Para cada temporada da lista 'temporadas' (representada por 't'), executa o método 'episodios()'
        // que retorna a lista de episódios dessa temporada. Em seguida, para cada episódio (representado por 'e'),
        // imprime no console o título do episódio usando 'System.out.println(e.titulo())'.
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Temporada(parametro) -> expressão
        //o parametro diz, cada uma das temporadas

        // Transforma a lista de nomes em um stream para aplicar operações funcionais:
// - 'stream()': inicia um fluxo de dados da lista.
// - 'sorted()': ordena os elementos do stream em ordem natural (alfabética).
// - 'limit(3)' limita o resultado aos 3 primeiros nomes da lista ordenada.
// - 'filter(n -> n.startsWith("J"))' filtra os nomes que começam com a letra "J" (case sensitive).
// - 'map(n -> n.toUpperCase())' transforma os nomes filtrados para letras maiúsculas.
// - 'forEach(System.out::println)': imprime cada nome ordenado no console.

//        List<String> nomes = Arrays.asList("joao", "Lucas", "maria", "laila");
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("J"))
//                .map((n -> n.toUpperCase()))
//                .forEach(System.out::println);
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
