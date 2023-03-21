import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int port = 8989;
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Enter request or type `end` to exit");
            String word = scanner.nextLine();
            if ("end".equals(word)) {
                System.out.println("Program terminated!");
                break;
            }

            if (!word.isEmpty()) {

                try (
                        Socket clientSocket = new Socket(hostName, port);
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                )
                {
                    out.println(word);
                    final String messageFromServer = in.readLine();
                    System.out.printf("Your results: %s", messageFromServer);
                }
            }
        }
    }

}
