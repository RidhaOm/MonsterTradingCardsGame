package Server;


import database.Db;
import model.Card;
import model.ScoreboardEntry;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

public class RequestHandler implements Runnable {

    private Socket clientConnection;

    public RequestHandler(Socket clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            InputStream inputStream = clientConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            OutputStream outputStream = clientConnection.getOutputStream();
            printWriter = new PrintWriter(outputStream);

            Request request = new RequestBuilder().buildRequest(bufferedReader);
            String content = HandleRequest(request);
            printWriter.write(
                    //new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, "Echo---" + request.getBody()).get() );
                    new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, "Response: " + content).get());

        } catch (IOException exception) {
            //exception.printStackTrace();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                    clientConnection.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public String HandleRequest(Request request) {
        String content = "fail " + request.getMethod();
        Method method = request.getMethod();
        String path = request.getPathname();
        if ((path.equals("/users")) && method == Method.POST) {
            content = handleCreateUser(request);
        } else if ((path.equals("/sessions")) && method == Method.POST) {
            content = handleLoginUser(request);
        } else if ((path.equals("/transactions/packages")) && method == Method.POST) {
            content = handleAcquirePackage(request);
        } else if ((path.equals("/packages")) && method == Method.POST) {
            content = handleCreatePackage(request);
        } else if ((path.equals("/cards")) && method == Method.GET) {
            content = handleShowStack(request);
        } else if ((path.equals("/deck")) && method == Method.GET) {
            content = handleShowDeck(request);
        } else if ((path.equals("/deck")) && method == Method.PUT) {
            content = configureDeck(request);
        } else if ((path.startsWith("/users/")) && method == Method.GET) {
            content = showUserData(request);
        } else if ((path.startsWith("/users/")) && method == Method.PUT) {
            content = editUserData(request);
        } else if ((path.equals("/stats")) && method == Method.GET) {
            content = handleStats(request);
        } else if ((path.equals("/score")) && method == Method.GET) {
            content = handleScoreBoard(request);
        } else if ((path.equals("/battles")) && method == Method.POST) {
            content = handleBattle(request);
        } else if ((path.equals("/tradings")) && method == Method.GET) {
            content = handleCheckTradingDeals(request);
        } else if ((path.equals("/tradings")) && method == Method.POST) {
            content = handleCreateTradingDeals(request);
        } else if ((path.startsWith("/tradings")) && method == Method.POST) {
            content = handleMakeTradingDeal(request);
        } else if ((path.startsWith("/tradings/")) && method == Method.DELETE) {
            content = handleDeleteTradingDeals(request);
        }

        return content;
    }

    public String getusername(Request request) {
        String authHeader = request.getHeaderMap().get("Authorization");
        String[] authParts = authHeader.split("-");
        String[] usernameParts = authParts[0].split(" ");
        String username = usernameParts[1];
        return username;
    }

    public String handleCreateUser(Request request) {
        String body = request.getBody();
        String[] keyValuePairs = body.split(",");
        String username = null;
        String password = null;
        String content;

        for (String keyValuePair : keyValuePairs) {
            String[] parts = keyValuePair.split(":");
            String key = parts[0].trim();
            String value = parts[1].trim();
            //The first Key begin with { so it have to be deleted:
            if (key.charAt(0) == '{') {
                key = key.substring(1);
            }
            //The last value end with } so it have to be deleted:
            if (value.charAt(value.length() - 1) == '}') {
                value = value.substring(0, value.length() - 1);
            }
            value = value.substring(1, value.length() - 1);// to remove the " at the begin and the end
            //System.out.println("Key: "+key+" Value: "+value);

            if (key.equals("\"Username\"")) {
                username = value;
            } else if (key.equals("\"Password\"")) {
                password = value;
            }
        }
        //Add Username and Password to the Database:
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "root");
        content = db.createUser(conn, username, password);
        return content;

    }

    public String handleLoginUser(Request request) {
        String body = request.getBody();
        String[] keyValuePairs = body.split(",");
        String username = null;
        String password = null;
        String content;

        for (String keyValuePair : keyValuePairs) {
            String[] parts = keyValuePair.split(":");
            String key = parts[0].trim();
            String value = parts[1].trim();
            //The first Key begin with { so it have to be deleted:
            if (key.charAt(0) == '{') {
                key = key.substring(1);
            }
            //The last value end with } so it have to be deleted:
            if (value.charAt(value.length() - 1) == '}') {
                value = value.substring(0, value.length() - 1);
            }
            value = value.substring(1, value.length() - 1);// to remove the " at the begin and the end

            if (key.equals("\"Username\"")) {
                username = value;
            } else if (key.equals("\"Password\"")) {
                password = value;
            }
        }
        //Add Username and Password to the Database:
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        content = db.loginUser(conn, username, password);
        return content;

    }

    public String handleCreatePackage(Request request) {
        String content;
        String username = getusername(request);
        if (username.equals("admin")) {
            Db db = new Db();
            Connection conn = db.connectToDb("postgres", "postgres", "");
            Card[] cards = new Card[5];
            String body = request.getBody();
            // Split the body into cards:
            String[] splitBody = body.split("\\{");
            for (int i = 0; i < 5; i++) {
                String s = splitBody[i + 1];// because first string is just the character [
                String id, name, cardType, elementType;
                double damage;
                id = s.substring(s.indexOf("\"Id\":\"") + 6, s.indexOf("\","));
                //remove the first type from the string
                s = s.substring(s.indexOf("\",") + 1);
                String element_name = s.substring(s.indexOf("\"Name\":\"") + 8, s.indexOf("\","));
                if ((element_name.length() > 5) && (element_name.substring(0, 5).equals("Water"))) {
                    elementType = "Water";
                    if (element_name.substring(5).equals("Spell")) {
                        cardType = "Spell";
                        name = "Spell";
                    } else {
                        cardType = "Monster";
                        name = element_name.substring(5);
                    }
                } else if ((element_name.length() > 4) && (element_name.substring(0, 4).equals("Fire"))) {
                    elementType = "Fire";
                    if (element_name.substring(4).equals("Spell")) {
                        cardType = "Spell";
                        name = "Spell";
                    } else {
                        cardType = "Monster";
                        name = element_name.substring(4);
                    }
                } else {
                    elementType = "Normal";
                    if (element_name.equals("Spell")) {
                        cardType = "Spell";
                        name = "Spell";
                    } else {
                        cardType = "Monster";
                        name = element_name;
                    }
                }
                s = s.substring(s.indexOf("\",") + 1);
                String damageString = s.substring(s.indexOf("\"Damage\": ") + 10, s.indexOf("}"));
                damage = Double.parseDouble(damageString);
                Card card = new Card(id, cardType, elementType, name, damage);
                db.createCard(conn, card);
                cards[i] = card;
            }
            content = db.createPackage(conn, cards);
        } else {
            content = "ERROR: " + username + " is not allowed to create a package.";
        }
        return content;
    }

    public String handleAcquirePackage(Request request) {
        String username = getusername(request);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content = db.acquirePackage(conn, username);
        return content;
    }

    public String handleShowStack(Request request) {
        String username = getusername(request);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content = db.showStack(conn, username);
        return content;
    }

    public String handleShowDeck(Request request) {
        String username = getusername(request);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content = db.showDeck(conn, username);
        return content;
    }

    public String configureDeck(Request request) {
        String body = request.getBody();
        // Remove the leading and trailing brackets
        body = body.substring(1, body.length() - 1);
        // Split the string by the comma and space characters
        String[] IDs = body.split(", ");
        Vector<String> cardsId = new Vector<String>(4);
        for (String id : IDs) {
            // Remove the leading and trailing "
            String cardId = id.substring(1, id.length() - 1);
            cardsId.add(cardId);
        }
        // Get the username:
        String username = getusername(request);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content = db.setDeck(conn, username, cardsId);
        return content;
    }

    public String showUserData(Request request) {
        String username = getusername(request);
        String usernamePath = request.getPathname().substring("/users/".length());
        String content;
        if (username.equals(usernamePath)) {
            Db db = new Db();
            Connection conn = db.connectToDb("postgres", "postgres", "");
            content = db.showUserData(conn, username);
        } else {
            content = "ERROR: the username in the path : " + usernamePath + " does not match with the username in the header: " + username;
        }
        return content;
    }

    public String editUserData(Request request) {
        String username = getusername(request);
        String usernamePath = request.getPathname().substring("/users/".length());
        String content;
        if (username.equals(usernamePath)) {
            String body = request.getBody();
            // Get the start index of the name
            int nameStartIndex = body.indexOf("\"Name\": \"") + "\"Name\": \"".length();
            // Delete first part from the body so that it begins from the name
            body = body.substring(nameStartIndex);
            // Get the end index of the name
            int nameEndIndex = body.indexOf("\"");
            // Extract the name from the body
            String name = body.substring(0, nameEndIndex);

            // The same logic with bio
            int bioStartIndex = body.indexOf("\"Bio\": \"") + "\"Bio\": \"".length();
            body = body.substring(bioStartIndex);
            int bioEndIndex = body.indexOf("\"");
            String bio = body.substring(0, bioEndIndex);

            // The same logic with bio image
            int imageStartIndex = body.indexOf("\"Image\": \"") + "\"Image\": \"".length();
            body = body.substring(imageStartIndex);
            int imageEndIndex = body.indexOf("\"");
            String image = body.substring(0, imageEndIndex);

            Db db = new Db();
            Connection conn = db.connectToDb("postgres", "postgres", "");
            content = db.editUserData(conn, username, name, bio, image);
        } else {
            content = "ERROR: the username in the path : " + usernamePath + " does not match with the username in the header: " + username;
        }
        return content;
    }

    public String handleStats(Request request) {
        String username = getusername(request);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content = db.getStats(conn, username);
        return content;
    }

    public String handleScoreBoard(Request request) {
        String content = "";
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        List<ScoreboardEntry> scoreboard = db.getScoreboard(conn);
        for (ScoreboardEntry entry : scoreboard) {
            content += "\n" + entry.getUsername() + ": " + entry.getELO_value();
        }
        return content;
    }

    public String handleBattle(Request request) {
        // Get the username of the user making the request
        String username = getusername(request);

        // Connect to the database
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");

        // Check if the user has a pending battle
        if (db.hasPendingBattle(conn, username)) {
            // The user has a pending battle, return a message indicating that the user is already in a battle
            return "You are already in a battle. Please wait for the current battle to finish before starting a new one.\n";
        } else {
            // The user does not have a pending battle, add the user to the queue
            db.addToQueue(conn, username);

            // Check if there is another user in the queue
            String opponentUsername = db.getNextInQueue(conn, username);
            if (opponentUsername != null) {
                // There is another user in the queue, start the battle
                String result = db.startBattle(conn, username, opponentUsername);

                db.deleteFromQueue(conn, username);
                db.deleteFromQueue(conn, opponentUsername);
                return result;
            } else {
                // There are no other users in the queue, return a message indicating that the user has been added to the queue
                return "You have been added to the queue. Please wait for an opponent to join.\n";
            }
        }
    }

    public String handleCheckTradingDeals(Request request) {
        String username = getusername(request);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content="";
        content += db.getCurrentTrades(conn, username);
        content += db.getCompletedTrades(conn, username);
        return content;
    }

    public String handleCreateTradingDeals(Request request) {
        String username = getusername(request);
        //Split the body:
        String body = request.getBody();
        String[] parts = body.split(",");
        String id = parts[0].split(":")[1];
        String cardToTrade = parts[1].split(":")[1];
        String type = parts[2].split(":")[1];
        String minimumDamage = parts[3].split(":")[1];
        //Clean the  variables:
        id = id.substring(2, id.length() - 1);
        cardToTrade = cardToTrade.substring(2, cardToTrade.length() - 1);
        type = type.substring(2, type.length() - 1);
        minimumDamage = minimumDamage.substring(1, minimumDamage.length() - 1);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content = db.addTrade(conn, id, username, cardToTrade, type, minimumDamage);

//        return "create trading deal. Body:\n"+id+"|"+cardToTrade+"|"+type+"|"+minimumDamage+"|";
        return content;
    }

    public String handleDeleteTradingDeals(Request request) {
        String id = request.getPathname().substring(10);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content=db.deleteTrade(conn, id);
        return content;
    }

    public String handleMakeTradingDeal(Request request) {
        String username = getusername(request);
        String tradeId = request.getPathname().substring(10);
        String cardToSellId = request.getBody().substring(1, request.getBody().length()-1);
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        String content=db.trade(conn, username, tradeId, cardToSellId);
        return content;
    }
}
