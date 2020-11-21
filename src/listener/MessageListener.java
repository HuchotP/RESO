package listener;

import javax.swing.EventListener;

/**
 * The interface Message listener.
 */
public interface MessageListener extends EventListener {

    /**
     * Message recu.
     *
     * @param e the e
     */
    public void messageRecu(MessageEvent e);

}