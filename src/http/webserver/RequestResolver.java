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

/**
 * Handles the work of answering the request
 */
public class RequestResolver{

    HashMap<String, Route> routeMap;

    /**
     * Default constructor for RequestResolver
     */
    public RequestResolver(){

        routeMap = new HashMap<String, Route>();

    }


    /**
     * Adds a POST route on @param path for customized behaviour
     * @param path : a route starting with /
     * @param route : an object implementing the Route interface
     */
    public void ajouterRoute(String path, Route route ){
        routeMap.put(path, route);
    }

    /**
     * Main method for answering a request. It does the job of routing the request to the correct resolver.a
     * @param r : the parsed request
     * @param s : The socket to which we should send the response
     * @param publicPath : the path of the public files folder 
     */
    public void resolveRequest(Request r, Socket s, String publicPath){
        if(routeMap.containsKey(r.getPath())){
            Route route = routeMap.get(r.getPath());
            route.execute(r, s);
        }else{
            if(r.getType() == Request.Type.GET){
                resolveGet(r, s, publicPath, false);
            }else if(r.getType() == Request.Type.PUT) {
                resolvePut(r, s, publicPath);
            } else if (r.getType() == Request.Type.HEAD){
                resolveGet(r, s, publicPath, true);
            }else if (r.getType() == Request.Type.DELETE){
                resolveDelete(r, s, publicPath);
            } else if(r.getType() == Request.Type.POST) {
                resolvePost(r, s, publicPath);
            }
        }
    }

    /**
     * Resolves a POST request by looking if it's registered. If not a 404 is sent.
     * @param r : the parsed request
     * @param s : The socket to which we should send the response
     * @param publicPath : the path of the public files folder 
     */
    public void resolvePost(Request r, Socket s, String publicPath){
        
        if(routeMap.containsKey(r.getPath())){
            Route route = routeMap.get(r.getPath());
            route.execute(r, s);
        }else{
            try {
                PrintWriter out = new PrintWriter(s.getOutputStream());
                out.println("HTTP/1.1 404 Not found");
                out.println("");
                out.println("<h1>Route not found on server</h1>");
                out.println("");
                out.flush();
                s.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Resolves a DELETE request by looking for the specified file in the public folder and deleting it if it exists.
     * @param r : the parsed request
     * @param s : The socket to which we should send the response
     * @param publicPath : the path of the public files folder 
     */
    public static void resolveDelete(Request r, Socket s, String publicPath) {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream());
            Path filePath = java.nio.file.Paths.get(publicPath, r.path);
            File file = filePath.toFile();
            System.out.println(filePath.toAbsolutePath().toString() + " " + filePath.toAbsolutePath().toString().lastIndexOf("\\"));
            File directory = new File(filePath.toAbsolutePath().toString().substring(0, filePath.toAbsolutePath().toString().lastIndexOf("\\")));
            if(!file.exists() || !directory.exists()) { //if file or directory does not exist c tout benef
                out.println("HTTP/1.1 201 No Content");
                out.println("");
                out.println("<h1>File does not exist</h1>");
            }else{
                if(file.delete()){
                    out.println("HTTP/1.1 204 No Content"); // c'est bon ya pu rien
                    out.println("");
                    out.println("<h1>File deleted</h1>");
                }
            }
            
            // this blank line signals the end of the headers
            out.println("");
            out.flush();
            s.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resolves a PUT request by looking for the specified file in the public folder overwriting it if it exists
     * or creating folders if needed.
     * @param r : the parsed request
     * @param s : The socket to which we should send the response
     * @param publicPath : the path of the public files folder 
     */
    public static void resolvePut(Request r, Socket s, String publicPath) {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream());
            boolean rewrite = false;
            Path filePath = java.nio.file.Paths.get(publicPath, r.path);
            File file = filePath.toFile();
            System.out.println(filePath.toAbsolutePath().toString() + " " + filePath.toAbsolutePath().toString().lastIndexOf("\\"));
            File directory = new File(filePath.toAbsolutePath().toString().substring(0, filePath.toAbsolutePath().toString().lastIndexOf("\\")));
            if(file.exists()) {
                rewrite = true;
            }
            if(!directory.exists()) {
                directory.mkdirs();
            }
            FileWriter fw = new FileWriter(file, false); //overwriting rather than appending (PUT definition)
            fw.write(r.content);
            fw.close();

            if(rewrite) {
                out.println("HTTP/1.1 204 No Content");
            } else {
                out.println("HTTP/1.0 201 Created");  
            }
            out.println("Content-Location: " + r.path);
            // this blank line signals the end of the headers
            out.println("");
            out.flush();
            s.close();


        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resolves a GET request by looking for the specified file in the public folder.
     * If the file is found it is sent. If the file is not found it tries to find the 
     * index.html in the specified folder. 
     * @param r : the parsed request
     * @param s : The socket to which we should send the response
     * @param publicPath : the path of the public files folder 
     */
    public static void resolveGet(Request request, Socket socket, String publicPath, boolean isHead) {
        
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
                out.println("Content-Length :" + file.length());
                out.println("Server: Bot");
                // this blank line signals the end of the headers
                out.println("");
                out.flush();
                if(!isHead) {
                    Files.copy(filePath, socket.getOutputStream());
                }
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