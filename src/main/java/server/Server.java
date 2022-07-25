package server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int SERVER_PORT = 8186;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket clientSocket = null;

    public static void main(String[] args){

        serverConnect();
        sendServer();

    }

    private static void serverConnect() {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            while (true) {
                System.out.println("Ожидание подключения...");

                clientSocket = serverSocket.accept();
                System.out.println("Подключение установлено!");

                out = new DataOutputStream(clientSocket.getOutputStream());
                in = new DataInputStream(clientSocket.getInputStream());
                sendServer();
                try {
                    while (true) {

                        String message = in.readUTF();

                        if (message.equals("/stop")) {
                            System.out.println("Сервер остановлен");
                            System.exit(0);
                        }
                        System.out.println("Клиент : " + message);
                        out.writeUTF(message);

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        clientSocket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendServer() {
        Thread s = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (true){
                try {
                    String messageServer = scanner.nextLine();
                    out.writeUTF("Сервер :" + messageServer.toUpperCase());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        s.setDaemon(true);
        s.start();
    }
}