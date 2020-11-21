///A Simple Web Server (WebServer.java)

package http.server;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * 
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 * 
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {

  /**
   * WebServer constructor.
   */

  public WebServer(){}

  protected void start() {
    ServerSocket s;

    final String publicPath = "../public";

    System.out.println("Webserver starting up on port 80");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());

        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.

        //GET / HTTP/1.0
        //GET /index.html HTTP/1.0
        //GET /index.js HTTP/1.0

        ArrayList<String> lineList = new ArrayList<String>();
        while (in.ready()) {
          String line = in.readLine();
          if(line.length() == 0) break;
          lineList.add(line);
          
        }

        if(lineList.size() == 0) {
          remote.close();
          continue;
        }

        Request r;
        r = new Request(lineList);
        System.out.println(r);

      

        if(r.isInvalid()) {
          // Send the response
          // Send the headers
          out.println("HTTP/1.0 403 OK");
          out.println("Content-Type: text/html");
          out.println("Server: Bot");
          // this blank line signals the end of the headers
          out.println("");
          // Send the HTML page
          out.println("<H1>Barre toi</H2>");
          out.flush();
          remote.close();
        }else{

          switch(r.getType()) {
            case Request.Type.GET:
              RequestResolver.resolveGet(r, remote, publicPath);
              break;
            default:
              break;
          }
          
        }

        
      } catch (Exception e) {
        System.out.println("Error: " + e);
        e.printStackTrace();
      }
    }
  }

  /**
   * Start the application.
   * 
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
