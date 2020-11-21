package server;

import server.SocketClient;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner; // Import the Scanner class to read text files

/**
 * The type Diffusion.
 */
public class Diffusion{

    private List<SocketClient> socketsClient;

    private LinkedList<String> historique;
    private File historiqueFile;
    private FileWriter historiqueFileWriter;

    private static Diffusion instance = null;

    private Diffusion(List<SocketClient> socketsClient){
        this.socketsClient = socketsClient;
        historique = new LinkedList<String>();
        historiqueFile = new File("historique");
        try {
            if(!historiqueFile.exists()) historiqueFile.createNewFile();
            Scanner historiqueFileReader = new Scanner(historiqueFile);
            while (historiqueFileReader.hasNextLine()) {
                String data = historiqueFileReader.nextLine();
                historique.add(data);
            }
            historiqueFileReader.close();
            historiqueFileWriter = new FileWriter("historique", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

    /**
     * Init.
     *
     * @param socketsClient the sockets client
     */
    public static void init(List<SocketClient> socketsClient) {
        instance = new Diffusion(socketsClient);
    }

    /**
     * Get instance diffusion.
     *
     * @return the diffusion
     */
    public static Diffusion getInstance(){
        return instance;
    }

    /**
     * Recevoir message.
     *
     * @param origin  the origin
     * @param message the message
     */
    public synchronized void recevoirMessage(SocketClient origin, String message){
        historique.add(message);
        try {
            historiqueFileWriter.append('\n');
            historiqueFileWriter.write(message);
            historiqueFileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        for(SocketClient socket : socketsClient){
            if(socket != origin){
                socket.envoyerMessage(message);
            }
        }
    }

    /**
     * Envoyer historique.
     *
     * @param client the client
     */
    public void envoyerHistorique(SocketClient client) {
        String payload = "";
        for(int i = 0; i < historique.size(); ++i) {
            payload += historique.get(i) + (i == historique.size() - 1 ? "" : "\r\n");
        }
        client.envoyerMessage(payload);
    }

    /**
     * Deconnexion socket.
     *
     * @param client the client
     */
    public void deconnexionSocket(SocketClient client){
        this.socketsClient.remove(client);
    }

}