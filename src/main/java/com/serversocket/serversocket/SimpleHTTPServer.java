package com.serversocket.serversocket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHTTPServer {
    public static void main(String[] args) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Listening on port 8080...");

        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.execute(new ClientHandler(clientSocket));
        }
    }
}


