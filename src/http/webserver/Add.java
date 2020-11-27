package http.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.*;

/**
 * Route for /ajouter. Takes an integer "value" as input and adds it to a global sum.
 */
public class Add implements Route {
    
    private int sum = 0;

    /**
     * Parses the "value" token in the JSON input and adds it, then answers with the new sum value.
     * @param r : the request content
     * @param s : the client socket
     */
    public void execute(Request r, Socket s) {
        System.out.println(r);
        int toAdd = r.jsonParameters.getInt("value");
        sum += toAdd;
        try {

            PrintWriter out = new PrintWriter(s.getOutputStream());
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type : application/json");
            out.println("");
            out.println("{\"newValue\":  "+ sum + "}");
            out.println("");
            out.flush();
            s.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}