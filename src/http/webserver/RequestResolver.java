package http.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.HashMap;


public class RequestResolver{

    HashMap<String, Route> routeMap;

    public RequestResolver(){

        routeMap = new HashMap<String, Route>();

    }


    public void ajouterRoute(String path, Route route ){
        routeMap.put(path, route);
    }

    public void resolveRequest(Request r, Socket s, String publicPath){
        if(routeMap.containsKey(r.getPath())){
            Route route = routeMap.get(r.getPath());
            route.execute(r, s);
        }else{
            if(r.getType() == Request.Type.GET){
                resolveGet(r, s, publicPath);
            }else if(r.getType() == Request.Type.PUT) {
                resolvePut(r, s, publicPath);
            } else{
                //renvoyer 404
            }
        }
    }

    public static void resolvePut(Request r, Socket s, String publicPath) {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream());
            boolean rewrite = false;
            Path filePath = java.nio.file.Paths.get(publicPath, r.path);
            File file = filePath.toFile();
            if(!file.exists()) {
                rewrite = true;
            }
            FileWriter fw = new FileWriter(file, false); //overwriting rather than appending (PUT definition)
            fw.write(r.content);
            fw.close();

            if(rewrite) {
                out.println("HTTP/1.1 204 No Content");
            } else {
                out.println("HTTP/1.0 201 Created");  
            }
            out.println("Content-Location: " + filePath.toString());
            // this blank line signals the end of the headers
            out.println("");
            out.flush();
            s.close();


        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void resolveGet(Request request, Socket socket, String publicPath) {
        
        try{
            

        PrintWriter out = new PrintWriter(socket.getOutputStream());


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
            socket.close();
            
        } else {
            
            
            Path filePath = java.nio.file.Paths.get(publicPath, request.path);

            File file = filePath.toFile();

            System.out.println(filePath.toAbsolutePath().toString());

            if(!file.exists()){
                out.println("HTTP/1.0 404 OK");
                out.println("Content-Type: text/html");
                out.println("Server: Bot");
                // this blank line signals the end of the headers
                out.println("");
                // Send the HTML page
                out.println("<h1>File not found</h1>");
                out.flush();
                socket.close();
                
                
            }else{
                String extension = "";

                int i = filePath.toAbsolutePath().toString().lastIndexOf('.');
                if (i > 0) {
                    extension = filePath.toAbsolutePath().toString().substring(i+1);
                } else {
                    filePath = java.nio.file.Paths.get(filePath.toAbsolutePath().toString(), "\\index.html");
                    System.out.println(filePath.toAbsolutePath().toString());
                    file = filePath.toFile();

                    if(!file.exists()){
                        out.println("HTTP/1.0 404 OK");
                        out.println("Content-Type: text/html");
                        out.println("Server: Bot");
                        // this blank line signals the end of the headers
                        out.println("");
                        // Send the HTML page
                        out.println("<h1>File not found</h1>");
                        out.flush();
                        socket.close();
                        return;
                        
                    } else extension = "html";
                }

                String contentType = "Content-Type: ";

                switch(extension){
                    case "mp3":
                        contentType += "audio/mpeg";
                        break;
                    case "html":
                        contentType += "text/html";
                        break;
                    case "js":
                        contentType += "application/javascript";
                        break;
                    case "css":
                        contentType += "text/css";
                        break;
                    case "png":
                        contentType += "image/png";
                        break;
                    case "gif":
                        contentType += "image/gif";
                        break;
                    case "jpeg":
                        contentType += "image/jpeg";
                        break;
                    case "jpg":
                        contentType += "image/jpeg";
                        break;
                    case "mpeg":
                        contentType += "video/mpeg";
                        break;
                    case "mp4":
                        contentType += "video/mp4";
                        break;
                    default:
                        contentType += "application/octet-stream";
                        break;
                }

                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);

                out.println("HTTP/1.0 200 OK");
                out.println(contentType);
                out.println("Server: Bot");
                // this blank line signals the end of the headers
                out.println("");
                out.flush();
                Files.copy(filePath, socket.getOutputStream());
                socket.getOutputStream().flush();
                reader.close();
                // Send the HTML page
                out.println();
                out.flush();
                socket.close();
                
            }

        }

    } catch(Exception e){
        e.printStackTrace();
    }
    }

}