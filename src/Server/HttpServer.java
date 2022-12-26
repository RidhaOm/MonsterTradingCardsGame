package Server;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    public void start() throws IOException {
        // Create a server socket to listen on the specified port
        ServerSocket serverSocket = new ServerSocket(10001);

        while (true) {
            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            // Create input and output streams to read from and write to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            // Read and process the HTTP request
            String request = readRequest(in);
            Map<String, String> headers = parseHeaders(request);
            String body = parseBody(request);
            System.out.println("Request: "+request+" || headers: "+headers+" || body: "+body);
            // Send a response back to the client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/plain");
            out.println();
            out.println("Hello, World!");

            // Close the connection
            clientSocket.close();
        }
    }

    private String readRequest(BufferedReader in) throws IOException {
        /*StringBuilder request = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            request.append(line).append("\r\n");
        }
        request.append("\r\n");
        if ((line = in.readLine()) != null) {
            request.append(line);
        }
        return request.toString();*/
        StringBuilder request = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            request.append(line).append("\r\n");
            if (line.isEmpty()) {
                break;
            }
        }
        return request.toString();
    }

    private Map<String, String> parseHeaders(String request) {
        Map<String, String> headers = new HashMap<>();
        String[] lines = request.split("\r\n");
        for (int i = 1; i < lines.length - 2; i++) {
            String[] parts = lines[i].split(": ");
            headers.put(parts[0], parts[1]);
        }
        return headers;
    }

    private String parseBody(String request) {
        int start = request.indexOf("\r\n\r\n") + 4;
        return request.substring(start);
    }

}
