package http.webserver;

import java.net.Socket;
/**
 * Interface for POST route resolving
 */
public interface Route {

    /**
     * Executes a process when called from a POST request
     * @param request : the request content
     * @param socket : the client socket
     */
    public void execute(Request request, Socket socket);
}
