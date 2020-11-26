package http.webserver;

import java.net.Socket;
public interface Route {

    public void execute(Request request, Socket socket);
}
