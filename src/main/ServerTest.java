package main;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerTest {
    private static final int PORT = 10001;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/monster_trading_cards";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "password";

    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Package> packages = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();
            String[] parts = request.split(" ");
            String method = parts[0];
            String path = parts[1];
            String body = "";
            if (parts.length > 2) {
                body = parts[2];
            }

            if (method.equals("POST") && path.equals("/users")) {
                handleCreateUser(body, out);
            } else if (method.equals("POST") && path.equals("/sessions")) {
                handleLogin(body, out);
            } else if (method.equals("POST") && path.equals("/packages")) {
                String[] headerParts = in.readLine().split(": ");
                String header = headerParts[0];
                String token = headerParts[1];
                if (header.equals("Authorization") && isTokenValid(token)) {
                    handleCreatePackage(body, out);
                } else {
                    out.println("HTTP/1.1 401 Unauthorized");
                }
            } else {
                out.println("HTTP/1.1 404 Not Found");
            }
        }
    }

    private static void handleCreateUser(String body, PrintWriter out) {
        String[] parts = body.split(", ");
        String username = parts[0].split(": ")[1];
        String password = parts[1].split(": ")[1];

        if (users.containsKey(username)) {
            out.println("HTTP/1.1 400 Bad Request");
            return;
        }

        User user = new User(username, password);
        users.put(username, user);
        out.println("HTTP/1.1 201 Created");
    }

    private static void handleLogin(String body, PrintWriter out) {
        String[] parts = body.split(", ");
        String username = parts[0].split(": ")[1];
        String password = parts[1].split(": ")[1];

        User user = users.get(username);
        if (user == null || !user.password.equals(password)) {
            out.println("HTTP/1.1 401 Unauthorized");
            return;
        }

        String token = UUID.randomUUID().toString();
        user.token = token;
        out.println("HTTP/1.1 200 OK");
        out.println("Authorization: " + token);
    }

    private static void handleCreatePackage(String body, PrintWriter out) {
        String[] parts = body.split(", ");
        String sender = parts[0].split(": ")[1];
        String recipient = parts[1].split(": ")[1];
        String contents = parts[2].split(": ")[1];

        Package pkg = new Package(sender, recipient, contents);
        packages.put(pkg.id, pkg);
        out.println("HTTP/1.1 201 Created");
        out.println("Location: /packages/" + pkg.id);
    }

    private static boolean isTokenValid(String token) {
        for (User user : users.values()) {
            if (user.token != null && user.token.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private static class User {
        String username;
        String password;
        String token;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private static class Package {
        String id;
        String sender;
        String recipient;
        String contents;

        public Package(String sender, String recipient, String contents) {
            this.id = UUID.randomUUID().toString();
            this.sender = sender;
            this.recipient = recipient;
            this.contents = contents;
        }
    }
}