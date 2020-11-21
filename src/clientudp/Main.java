/***
 * Main
 * Main TCP chat Client class 
 * Date: 17/11/20
 * Authors: 
 */
package clientudp;

import java.io.*;
import java.net.*;


/**
 * The type Main.
 */
public class Main {


    /**
     * main method
     * creates connection to server, sends messages and handles incoming messages
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {

        InetAddress groupAddr = null;
        Integer groupPort = null; 
        MulticastSocket socket = null;

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
        }

        try {
              // creation socket ==> connexion
            groupAddr = InetAddress.getByName(args[0]);
            groupPort = Integer.parseInt(args[1]);
            socket = new MulticastSocket(groupPort);

            socket.joinGroup(groupAddr);

            System.out.println("Client connected to address : " + groupAddr + " at port : " + groupPort);

            EnvoiThread e = new EnvoiThread(socket, groupAddr);
            ReceptionThread r = new ReceptionThread(socket);
            e.start();
            r.start();
        
            e.join();
            r.join();
	    
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
        catch(InterruptedException e){
            System.err.println("Error in thread : ");
            e.printStackTrace();
            System.exit(1);
        }
                             
    }
}


