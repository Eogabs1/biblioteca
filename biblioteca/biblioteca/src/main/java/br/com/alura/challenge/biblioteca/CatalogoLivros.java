package br.com.alura.challenge.biblioteca;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class CatalogoLivros {

    static final String BASE_URL = "https://gutendex.com/books/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n==== Catálogo de Livros - Gutendex ====");
            System.out.println("1. Buscar livros por título");
            System.out.println("2. Listar livros populares");
            System.out.println("3. Buscar livros por autor");
            System.out.println("4. Ver detalhes de um livro pelo ID");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    System.out.print("Digite o título: ");
                    String titulo = scanner.nextLine();
                    buscarLivros("search=" + titulo);
                    break;
                case 2:
                    buscarLivros("");
                    break;
                case 3:
                    System.out.print("Digite o nome do autor: ");
                    String autor = scanner.nextLine();
                    buscarLivros("search=" + autor);
                    break;
                case 4:
                    System.out.print("Digite o ID do livro: ");
                    int id = scanner.nextInt();
                    buscarLivroPorID(id);
                    break;
                case 5:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 5);

        scanner.close();
    }

    public static void buscarLivros(String query) {
        try {
            URL url = new URL(BASE_URL + "?" + query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer resposta = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                resposta.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(resposta.toString());
            JSONArray resultados = json.getJSONArray("results");

            for (int i = 0; i < resultados.length(); i++) {
                JSONObject livro = resultados.getJSONObject(i);
                System.out.println("\nID: " + livro.getInt("id"));
                System.out.println("Título: " + livro.getString("title"));

                JSONArray autores = livro.getJSONArray("authors");
                for (int j = 0; j < autores.length(); j++) {
                    JSONObject autor = autores.getJSONObject(j);
                    System.out.println("Autor: " + autor.getString("name"));
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar livros: " + e.getMessage());
        }
    }

    public static void buscarLivroPorID(int id) {
        try {
            URL url = new URL(BASE_URL + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer resposta = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                resposta.append(inputLine);
            }
            in.close();

            JSONObject livro = new JSONObject(resposta.toString());
            System.out.println("\nTítulo: " + livro.getString("title"));

            JSONArray autores = livro.getJSONArray("authors");
            for (int j = 0; j < autores.length(); j++) {
                JSONObject autor = autores.getJSONObject(j);
                System.out.println("Autor: " + autor.getString("name"));
            }

            JSONArray idiomas = livro.getJSONArray("languages");
            System.out.println("Idiomas: " + idiomas.join(", "));

            JSONObject formatos = livro.getJSONObject("formats");
            System.out.println("Link para leitura (HTML): " + formatos.optString("text/html", "Não disponível"));

        } catch (Exception e) {
            System.out.println("Erro ao buscar o livro: " + e.getMessage());
        }
    }
}
