/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import java.io.*;
import java.net.*;

import server.Diffusion;

/**
 * The Class Socket client.
 */
public class SocketClient
	extends Thread {
	
	private Socket clientSocket;

	/**
	 * Instantiates a new Socket client.
	 *
	 * @param s the socket of the client
	 */
	SocketClient(Socket s) {
		this.clientSocket = s;
	}

 	/**
	* Initiates client connection by sending it the history,
	* then handling message broadcasting on receive.
	* Also handles deconnection
  	* @param clientSocket the client socket
  	*/
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
			
			//Envoie de l'historique avant toute chose

			Diffusion.getInstance().envoyerHistorique(this);

    		while (true) {
    		  String line = socIn.readLine();
    		  if(line != null){
				  Diffusion.getInstance().recevoirMessage(this, line);

			  }
    		}
    	} catch (Exception e) {
        	System.out.println("Socket déconnectée");
        }

    	try{
    		clientSocket.close();

			Diffusion.getInstance().deconnexionSocket(this);

		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sends a message.
	 *
	 * @param message the message
	 */
	public void envoyerMessage(String message){
        try{
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            socOut.println(message);

        }catch (Exception e) {
        	System.err.println("Error in SocketClient:" + e); 
        }
    }

    
  
  }

  
