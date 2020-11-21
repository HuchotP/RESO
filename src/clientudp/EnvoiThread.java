package clientudp;


import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * The type Envoi thread.
 */
public class EnvoiThread extends Thread{

    private MulticastSocket socket;
    private InetAddress group;

    /**
     * Instantiates a new Envoi thread.
     *
     * @param socket the socket
     * @param group  the group
     */
    public EnvoiThread(MulticastSocket socket, InetAddress group){

        this.socket = socket;
        this.group = group;

    }

    public void run(){
        BufferedReader stdIn = null;
        try{
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            InetAddress groupAddr = socket.getLocalAddress();
            Integer groupPort = socket.getLocalPort();


            while(true){
                String line=stdIn.readLine();
                if (line.equals(".")) break;
                DatagramPacket msg = new DatagramPacket(line.getBytes(), line.length(), group, groupPort);  
                socket.send(msg);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
            

        }
    }
}