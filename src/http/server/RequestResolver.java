import java.nio.file.Path;


public class RequestResolver{

    private RequestResolver(){}

    public static void resolveGet(Request request, Socket socket, String publicPath) {
        
        PrintWriter out = new PrintWriter(remote.getOutputStream());


        if(request.isInvalid()) {
            // Send the response
            // Send the headers
            out.println("HTTP/1.0 403 OK");
            out.println("Content-Type: text/html");
            out.println("Server: Bot");
            // this blank line signals the end of the headers
            out.println("");
            // Send the HTML page
            out.println("<h1>Access forbidden</h1>");
            out.flush();
            remote.close();
            
        } else {
            
            Path filePath = java.nio.file.Paths.get(publicPath, request.path);

            File file = filePath.toFile();

            if(!file.exists()){
                out.println("HTTP/1.0 404 OK");
                out.println("Content-Type: text/html");
                out.println("Server: Bot");
                // this blank line signals the end of the headers
                out.println("");
                // Send the HTML page
                out.println("<h1>File not found</h1>");
                out.flush();
                remote.close();
            }else{
                String extension = "";

                int i = filePath.getAbsolutePath().lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                } else {
                    
                }
            }

        }

        
    }

}