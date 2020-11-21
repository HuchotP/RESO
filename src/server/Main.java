/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package server;

import java.io.*;
import java.net.*;
import java.util.*;

import server.SocketClient;
import server.Diffusion;

/**
 * The type Main.
 */
public class Main  {

    /**
     * main method
     *
     * @param args the args
     */
    public static void main(String args[]){
		ServerSocket listenSocket;
		LinkedList<SocketClient> socketsClient = new LinkedList<SocketClient>();
		Diffusion.init(socketsClient);
		Diffusion instance = Diffusion.getInstance();
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			SocketClient sc = new SocketClient(clientSocket);
			socketsClient.add(sc);
			sc.start();
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }

  
