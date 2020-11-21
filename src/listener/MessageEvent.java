package listener;

import server.SocketClient;

/**
 * The type Message event.
 */
public class MessageEvent {
    
    private SocketClient client;
    private String message;

    /**
     * Instantiates a new Message event.
     *
     * @param client  the client
     * @param message the message
     */
    public MessageEvent(SocketClient client, String message) {
        this.client = client;
        this.message = message;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public SocketClient getClient() {
        return client;
    }
}