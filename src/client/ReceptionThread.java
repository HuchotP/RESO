package client;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The Recetion thread listening for incoming messages.
 */
public class ReceptionThread extends Thread {

    private Socket socket;
    private Main main;
    private BufferedReader socIn;

    /**
     * Instantiates a new Reception thread.
     *
     * @param socket the server socket
     * @param main         the main
     */
    public ReceptionThread(Socket socket, Main main) {
        this.main = main;
        this.socket = socket;
    }

    /**
     * Method that will be run when Thread.start() is called.
     */
    public void run() {
        socIn = null;
        try {
            socIn = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
                
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