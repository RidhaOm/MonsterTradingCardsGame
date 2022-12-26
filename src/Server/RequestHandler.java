package Server;


import database.Db;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;

public class RequestHandler implements  Runnable{

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
            String content=HandleRequest(request);
            printWriter.write(
                    //new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, "Echo---" + request.getBody()).get() );
                    new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, "Echo---" +content).get() );

        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
        finally {
            try {
                if (printWriter != null){
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                    clientConnection.close();
                }
            }catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
    }

    public String HandleRequest(Request request) {
        String content="fail "+request.getMethod();
        Method method=request.getMethod();
        String path=request.getPathname();
        if( (path.equals("/users")) && method==Method.POST ){
            content= HandleCreateUser(request);
        }
        else if( (path.equals("/sessions")) && method==Method.POST ){
            content= "Login Users";
        }
        else if( (path.equals("/packages")) && method==Method.POST ){
            content= "create packages (done by \"admin\") OR acquire packages";
        }
        else if( (path.equals("/cards")) && method==Method.GET ){
            content= "show all acquired cards";
        }
        else if( (path.equals("/deck")) && method==Method.GET ){
            content= "show deck";
        }
        else if( (path.equals("/deck")) && method==Method.PUT ){
            content= "configure deck";
        }
        else if( (path.equals("/users")) && method==Method.GET ){
            content= "edit user data(Show)";
        }
        else if( (path.equals("/users")) && method==Method.PUT ){
            content= "edit user data";
        }
        else if( (path.equals("/stats")) && method==Method.GET ){
            content= "stats";
        }
        else if( (path.equals("/score")) && method==Method.GET ){
            content= "scoreboard";
        }
        else if( (path.equals("/battles")) && method==Method.POST ){
            content= "battle";
        }
        else if( (path.equals("/tradings")) && method==Method.GET ){
            content= "check trading deals";
        }
        else if( (path.equals("/tradings")) && method==Method.POST ){
            content= "create trading deal";
        }
        else if( (path.equals("/tradings")) && method==Method.DELETE ){
            content= "delete trading deals";
        }

        return content;
    }
    public String HandleCreateUser(Request request){
        String body=request.getBody();
        String[] keyValuePairs = body.split(",");
        String username = null;
        String password = null;
        String content;

        for (String keyValuePair : keyValuePairs) {
            String[] parts = keyValuePair.split(":");
            String key = parts[0].trim();
            String value = parts[1].trim();
            //The first Key begin with { so it have to be deleted:
            if(key.charAt(0)=='{') {
                key=key.substring(1);
            }
            //The last value end with } so it have to be deleted:
            if(value.charAt(value.length() - 1)=='}') {
                value=value.substring(0,value.length()-1);
            }
            value=value.substring(1,value.length()-1);// to remove the " at the begin and the end
            System.out.println("Key: "+key+" Value: "+value);

            if (key.equals("\"Username\"")) {
                username = value;
            } else if (key.equals("\"Password\"")) {
                password = value;
            }
        }
        //Add Username and Password to the Database:
        Db db = new Db();
        Connection conn=db.connectToDb("postgres", "postgres", "root");
        content= db.createUser(conn,username,password);
        return content;

    }
}
