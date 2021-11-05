import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
//        Написать клиент - серверное приложение с возможностью бана пользователя со стороны сервера и
//        подсчетом количества сообщений от конкретного ip.
        try {
            clientSocket = new Socket("localhost", 4000);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            out.write("stop" + "\n");
            out.flush();
            new ReadMsg().start();
            new WriteMsg().start();

//            for (int i = 0; i < 5; i++) {
//                clientSocket = new Socket("localhost", 4000);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//
//                out.write("stop" + "\n");
//                out.flush();
//
//                in.close();
//                out.close();
//            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private static class ReadMsg extends Thread {

        @Override
        public void run() {

            String str;

            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("stop")) {
                        break;
                    }
                    System.out.println(str);
                }
            } catch (IOException e) {

            }
        }
    }

    public static class WriteMsg extends Thread {

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line;
                try {
                    line = scanner.nextLine();
                    out.write(line + "\n");
                    out.flush();
                    if (line.equals("stop")) {
                        break;
                    }
                } catch (IOException e) {

                }
            }
        }
    }
}