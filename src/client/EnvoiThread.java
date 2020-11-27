package client;


import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The type Envoi thread.
 */
public class EnvoiThread extends Thread{

    private Socket socket;
    private Main main;

    /**
     * Instantiates a new Envoi thread.
     *
     * @param socket the socket
     * @param main   the main
     */
    public EnvoiThread(Socket socket, Main main){

        this.socket = socket;


        this.main = main;

    }

    /**
     * @deprecated
     * Method that will be run when Thread.start() is called.
     * This is useless when running with a GUI because the thread listening for user input
     * is the GUI itself.
     */
    @Deprecated
    /*public void run(){
        
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        try{
            socOut= new PrintStream(socket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            while(true){
                String line=stdIn.readLine();
                if (line.equals(".")) break;
                socOut.println(line);
                main.ajouterMessage(line);
            }


        }catch(Exception e){
            e.printStackTrace();


        }
    }*/

    /**
     * Envoyer message.
     *
     * @param message the message
     */
    public synchronized void envoyerMessage(String message){

        try{
            PrintStream socOut = new PrintStream(socket.getOutputStream());

            socOut.println(message);


            main.ajouterMessage("Message envoyé : " + message + "\r\n");

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}