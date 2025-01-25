package com.serversocket.serversocket;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;
import java.net.InetAddress;


public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static List<String> todos = new ArrayList<>();
    private static final String MOCK_API_KEY = "API_KEY";

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             OutputStream output = clientSocket.getOutputStream()) {

            RequestParser request = new RequestParser(reader);
            String path = request.getPath();
            logRequest(request.getMethod() + " " + request.getPath(), clientSocket);

            if (path.equals("/")) {
                path = "/index.html";
            }

            if (path.startsWith("/api/time")) {
                handleApiTime(output);
            } else if ("GET".equals(request.getMethod()) && request.getPath().startsWith("/api/weather")) {
                handleWeatherRequest(writer, clientSocket.getInetAddress());
            } else if (request.getPath().startsWith("/api/todos")) {
                handleTodoRequest(writer, request.getMethod(), request.getQueryParams());
            } else {
                serveStaticFile(output, path);
            }

            logClientIP(clientSocket);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleApiTime(OutputStream output) throws IOException {
        String jsonResponse = "{ \"time\": \"" + new Date() + "\" }";
        sendResponse(output, "200 OK", "application/json", jsonResponse.getBytes());
    }

    private void serveStaticFile(OutputStream output, String filePath) throws IOException {
        File file = new File("src/main/resources/Webroot" + filePath);
        if (file.exists() && !file.isDirectory()) {
            String contentType = getContentType(filePath);
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            sendResponse(output, "200 OK", contentType, fileBytes);
        } else {
            sendResponse(output, "404 Not Found", "text/plain", "404 Not Found".getBytes());
        }
    }

    private void sendResponse(OutputStream output, String status, String contentType, byte[] content) throws IOException {
        output.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        output.write(("Content-Type: " + contentType + "\r\n").getBytes());
        output.write("\r\n".getBytes());
        output.write(content);
        output.flush();
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".js")) return "application/javascript";
        return "text/plain";
    }

    private void logRequest(String request, Socket clientSocket){
        String clientIP = clientSocket.getInetAddress().getHostAddress();
        try (FileWriter logWriter = new FileWriter("access.log", true);
             BufferedWriter bufferedWriter = new BufferedWriter(logWriter))
        {
            bufferedWriter.write("[" + new Date() + "] - IP: " + clientIP + " - Request: " + request);
            bufferedWriter.newLine();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void logClientIP(Socket clientSocket) {
        String clientIP = clientSocket.getInetAddress().getHostAddress();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ip_log.txt", true))) {
            writer.write("[" + new Date() + "] - IP: " + clientIP);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleTodoRequest(PrintWriter writer, String method, Map<String, String> queryParams) {
        if (method.equals("GET")) {
            writer.println("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + new Gson().toJson(todos));
        } else if (method.equals("POST")) {
            String task = queryParams.get("task");
            if (task != null && !task.isEmpty()) {
                todos.add(task);
                writer.println("HTTP/1.1 201 Created\r\n\r\nTask added.");
            } else {
                writer.println("HTTP/1.1 400 Bad Request\r\n\r\nTask cannot be empty.");
            }
        } else if (method.equals("DELETE")) {
            String task = queryParams.get("task");
            if (todos.remove(task)) {
                writer.println("HTTP/1.1 200 OK\r\n\r\nTask deleted.");
            } else {
                writer.println("HTTP/1.1 404 Not Found\r\n\r\nTask not found.");
            }
        } else if (method.equals("PUT")){
            String task = queryParams.get("task");
            String updatedTask = queryParams.get("updatedTask");
            if (task != null && updatedTask != null){
                int index = todos.indexOf(task);
                if(index != -1){
                    todos.set(index, updatedTask);
                    writer.println("HTTP/1.1 200 OK\r\n\r\nTask updated.");
                } else {
                    writer.println("HTTP/1.1 404 Not Found\r\n\r\nTask not found.");
                }
            } else{
                writer.println("HTTP/1.1 400 Bad Request\r\n\r\nTask and updated task cannot be empty");
            }
        }
    }

    private void handleWeatherRequest(PrintWriter writer, InetAddress clientAddress) {
        String location = getMockLocation(clientAddress);
        Map<String, String> weatherData = getMockWeatherData(location);
        Gson gson = new Gson();
        writer.println("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + gson.toJson(weatherData));
    }

    private String getMockLocation(InetAddress clientAddress) {
        String ipAddress = clientAddress.getHostAddress();
        if (ipAddress.startsWith("127.0.0")) {
            return "Localhost City";
        }
        if(ipAddress.startsWith("192.168.")){
            return "Local Network City";
        }
        return "Global City";
    }

    private Map<String, String> getMockWeatherData(String location) {
        if (location.equals("Localhost City")) {
            return Map.of(
                    "city", "Localhost City",
                    "temperature", "25°C",
                    "condition", "Sunny"
            );
        }
        if (location.equals("Local Network City")) {
            return Map.of(
                    "city", "Local Network City",
                    "temperature", "15°C",
                    "condition", "Cloudy"
            );
        }
        return Map.of(
                "city", "Global City",
                "temperature", "20°C",
                "condition", "Partly Cloudy"
        );
    }

}