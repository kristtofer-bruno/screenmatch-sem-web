package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=1d4a89c5";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    public void exibeMenu(){
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);

            opcao = sc.nextInt();
            sc.nextLine();
            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        dadosSeries.add(dados);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        List<Serie> series = new ArrayList<>();
        series = dadosSeries.stream()
                .map(d -> new Serie(d))
                        .collect(Collectors.toList());
        series.stream()
                        .sorted(Comparator.comparing(Serie::getGenero))
                                .forEach(System.out::println);
    }



// BLOCO DE CÓDIGO FEITO ANTES PARA TESTE
//        var nomeSerie = sc.nextLine();
//
//        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
//
//        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//        System.out.println(dados);
//
//        List<DadosTemporada> temporadas = new ArrayList<>();
//        for(int i = 1; i <= dados.totalTemporadas(); i++){
//			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
//			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//			temporadas.add(dadosTemporada);
//		}
//
//		temporadas.forEach(System.out::println);
//        // :: = o mesmo que temporadas.forEach(t -> System.out.println(t));
//
////        for(int i = 0; i < dados.totalTemporadas(); i++){
////            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
////            for(int j = 0; j < episodiosTemporada.size(); j++){
////                System.out.println(episodiosTemporada.get(j).titulo());
////            }
////        }
//
//        //lambda -> funções que terão um parametro utilizando uma única letra para declarar o parametro
//        // Para cada temporada da lista 'temporadas' (representada por 't'), executa o método 'episodios()'
//        // que retorna a lista de episódios dessa temporada. Em seguida, para cada episódio (representado por 'e'),
//        // imprime no console o título do episódio usando 'System.out.println(e.titulo())'.
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
//
//        //Temporada(parametro) -> expressão
//        //o parametro diz, cada uma das temporadas
//
//        // Transforma a lista de nomes em um stream para aplicar operações funcionais:
//// - 'stream()': inicia um fluxo de dados da lista.
//// - 'sorted()': ordena os elementos do stream em ordem natural (alfabética).
//// - 'limit(3)' limita o resultado aos 3 primeiros nomes da lista ordenada.
//// - 'filter(n -> n.startsWith("J"))' filtra os nomes que começam com a letra "J" (case sensitive).
//// - 'map(n -> n.toUpperCase())' transforma os nomes filtrados para letras maiúsculas.
//// - 'forEach(System.out::println)': imprime cada nome ordenado no console.
//
////        List<String> nomes = Arrays.asList("joao", "Lucas", "maria", "laila");
////        nomes.stream()
////                .sorted()
////                .limit(3)
////                .filter(n -> n.startsWith("J"))
////                .map((n -> n.toUpperCase()))
////                .forEach(System.out::println);
//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
////        System.out.println("\nTop 10 episódios: ");
////        dadosEpisodios.stream()
////                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
////                .peek(e -> System.out.println("Primeiro filtro(N/A " + e))
////                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
////                .peek(e -> System.out.println("Ordenação " + e))
////                .limit(10)
////                .peek(e -> System.out.println("Limite " + e))
////                .map(e-> e.titulo().toUpperCase())
////                .peek(e -> System.out.println("Mapeamento " + e))
////                .forEach(System.out::println);
//
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodio(t.numero(), d))
//                ).collect(Collectors.toList());
//
//        episodios.forEach(System.out::println);
//
////        System.out.println("Digite um trecho do título do episódio: ");
////        var trechoTitulo = sc.nextLine();
////        Optional<Episodio> episodioBuscado = episodios.stream()
////                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
////                .findFirst();
////        if(episodioBuscado.isPresent()){
////            System.out.println("Episódio encontrado!");
////            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
////        }
////        else {
////            System.out.println("Episódio não encontrado!");
////        }
////
////        System.out.println("A partir de que ano deseja ver os episódios?: ");
////        var ano = sc.nextInt();
////        sc.nextLine();
////
////        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
////
////        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////        episodios.stream()
////                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
////                .forEach(e -> System.out.println(
////                        "Temporada: " + e.getTemporada() +
////                                " Episódio: " + e.getTitulo() +
////                                " Data lançamento: " + e.getDataLancamento().format(formatador)
////                ));
//
//        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));
//        System.out.println(avaliacoesPorTemporada);
//
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e-> e.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//        System.out.println("Média: " + est.getAverage());
//        System.out.println("Melhor episódio: " + est.getMax());
//        System.out.println("Pior episódio: " + est.getMin());
//        System.out.println("Quantidade: " + est.getCount());
}
