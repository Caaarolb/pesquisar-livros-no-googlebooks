import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class GoogleBooksSearch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Pesquisar por um livro");
            System.out.println("2. Sair do programa");
            System.out.print("Escolha uma opção: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    // Solicitar título do livro
                    System.out.print("\nDigite o título do livro: ");
                    String bookTitle = scanner.nextLine();

                    // Criar URL e fazer a requisição
                    String url = "https://www.googleapis.com/books/v1/volumes?q=" + bookTitle.replace(" ", "+");
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .GET()
                            .build();

                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                        JsonArray items = jsonResponse.getAsJsonArray("items");

                        if (items != null && items.size() > 0) {
                            // Exibir informações do primeiro livro
                            JsonObject firstItem = items.get(0).getAsJsonObject();
                            JsonObject volumeInfo = firstItem.getAsJsonObject("volumeInfo");

                            System.out.println("\nInformações do livro:");
                            System.out.println("Título: " + volumeInfo.get("title").getAsString());
                            if (volumeInfo.has("authors")) {
                                System.out.println("Autores: " + volumeInfo.get("authors").getAsJsonArray());
                            }
                            if (volumeInfo.has("publishedDate")) {
                                System.out.println("Data de publicação: " + volumeInfo.get("publishedDate").getAsString());
                            }
                            if (volumeInfo.has("description")) {
                                System.out.println("Descrição: " + volumeInfo.get("description").getAsString());
                            }
                        } else {
                            System.out.println("Nenhum livro encontrado para o título fornecido.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao consultar a API: " + e.getMessage());
                    }
                    break;

                case "2":
                    // Chama o método para exibir a mensagem de encerramento
                    showExitMessage();
                    running = false;
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    // Método para exibir a mensagem de encerramento
    public static void showExitMessage() {
        System.out.println("\n" + "=".repeat(22) + " 💊 Programa encerrado! 💊 " + "=".repeat(20));
        System.out.println("Desenvolvido por: Jeisa Boaventura");
        System.out.println("GitHub: https://github.com/Caaarolb");
        System.out.println("LinkedIn: https://www.linkedin.com/in/-caroline-boaventura/");
        System.out.println("=" + "=".repeat(68) + "\n");
    }
}
