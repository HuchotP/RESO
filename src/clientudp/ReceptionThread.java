package clientudp;

import java.net.Socket;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

/**
 * The type Reception thread.
 */
public class ReceptionThread extends Thread {

    private MulticastSocket serverSocket;

    /**
     * Instantiates a new Reception thread.
     *
     * @param serverSocket the server socket
     */
    public ReceptionThread(MulticastSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            

            while(true) {
                byte[] buf = new byte[1000];
                DatagramPacket recv = new DatagramPacket(buf, buf.length); 
                serverSocket.receive(recv);
                System.out.println("Message recçu : " + new String(recv.getData(), StandardCharsets.UTF_8));
                
            } 
        }catch (Exception e) {
            System.err.println("Error in ReceptionThread:" + e); 
            // socIn.close();
        }
        // socIn.close();

    }

}