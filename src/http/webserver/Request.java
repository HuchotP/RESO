package http.webserver;

import java.util.*;
import org.json.*;

/**
 * Handles a raw request body and parses it in a simpler way.
 * 
 */
public class Request {

    public enum Type{
        GET,
        POST,
        PUT,
        DELETE,
        HEAD
    };

    Type requestType;
    boolean invalid;
    String path;
    String protocol;
    HashMap<String, String> headers;
    HashMap<String, String> formParameters;
    JSONObject jsonParameters;
    String content;

    /**
     * Default constructor for Request
     */
    public Request() {
        
    }

    /**
     * Constructor for Request
     * @param request : a list containing all headers
     * @param content : body of the request if it has one 
     */
    public Request(List<String> request, String content) {
        this.content = content;
        headers = new HashMap<String, String>();
        invalid = false;
        
        String[] requestCore = request.get(0).trim().split(" ");

        switch (requestCore[0].trim()) {
            case "GET":
                requestType = Type.GET;
                break;
            case "POST":
                requestType = Type.POST;
                break;
            case "PUT":
                requestType = Type.PUT;
                break;
            case "DELETE":
                requestType = Type.DELETE;
                break;
            case "HEAD":
                requestType = Type.HEAD;
                break;
            default:
                invalid = true;
                break;
        }
        

        path = requestCore[1].trim();

        if(path.indexOf("..") != -1){
            invalid = true;
        }

        protocol = requestCore[2].trim();

        for(int i = 1; i < request.size() - 1; i++){

            String[] header = request.get(i).trim().split(":", 2);

            if(header.length > 1){
                headers.put(header[0].trim(), header[1].trim());
            }
        }

        if(requestType == Type.POST || requestType == Type.PUT){
            if(headers.get("Content-Type").equals("application/x-www-form-urlencoded")){

                formParameters = new HashMap<>();

                String[] params = content.split("&");

                for(int i = 0; i < params.length; i++){

                    String[] param = params[i].split("=");

                    formParameters.put(param[0], param[1]);

                }

            }

            if(headers.get("Content-Type").equals("application/json")){

                jsonParameters = new JSONObject(content.substring(content.indexOf("{", 0)).substring(0, content.lastIndexOf("}") + 1));

            }
        }

    }

    /**
     * Returns true if the request is invalid (i.e., no valid type or seeks a ressource in parent folder "..")
     * @return boolean
     */
    public boolean isInvalid() {
        return invalid;
    }

    /**
     * Returns the type of the request (i.e., GET, POST...)
     * @return Request.Type
     */
    public Type getType() {
        return requestType;
    }

    /**
     * Returns the path asked by the request
     * @return String
     */
    public String getPath(){
        return path;
    }

    /**
     * Returns the request in String format for displaying and debugging
     * @return String
     */
    public String toString() {
        String type = headers.get("Content-Type");
        String ret ="";
        ret += requestType + " " + path + " " + protocol + "\r\n";
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ret += (pair.getKey() + ": " + pair.getValue()) + "\r\n";
            it.remove(); // avoids a ConcurrentModificationException
        }
        if(requestType == Type.POST || requestType == Type.PUT){
            if(type.equals("application/x-www-form-urlencoded")){
                
                it = formParameters.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    ret += (pair.getKey() + ": " + pair.getValue()) + "\r\n";
                    it.remove(); // avoids a ConcurrentModificationException
                }

            }

            else if(type.equals("application/json")){
                ret += jsonParameters.toString();

            }
        }

        return ret;
    }

}