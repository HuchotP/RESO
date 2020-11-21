package client;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The type Reception thread.
 */
public class ReceptionThread extends Thread {

    private Socket serverSocket;
    private Main main;
    private BufferedReader socIn;

    /**
     * Instantiates a new Reception thread.
     *
     * @param serverSocket the server socket
     * @param main         the main
     */
    public ReceptionThread(Socket serverSocket, Main main) {
        this.main = main;
        this.serverSocket = serverSocket;
    }

    public void run() {
        socIn = null;
        try {
            socIn = new BufferedReader(
                new InputStreamReader(serverSocket.getInputStream()));
                
            while(true) {
                String line = socIn.readLine();
                main.ajouterMessage("Message reçu : " + line + "\r\n");
            } 
        }catch (Exception e) {
            System.err.println("Error in ReceptionThread:" + e); 
            // socIn.close();
        }
        // socIn.close();

    }

    /**
     * Stop thread.
     */
    public void stopThread(){
        try{

            this.interrupt();


        }catch (Exception e){
             e.printStackTrace();
        }


    }

}