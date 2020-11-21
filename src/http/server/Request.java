package http.server;

import java.util.*;

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

    public Request() {

    }

    public Request(List<String> request) {
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

    }

    public boolean isInvalid() {
        return invalid;
    }

    public Type getType() {
        return requestType;
    }

    public String toString() {
        String ret ="";
        ret += requestType + " " + path + " " + protocol + "\r\n";
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ret += (pair.getKey() + ": " + pair.getValue()) + "\r\n";
            it.remove(); // avoids a ConcurrentModificationException
        }

        return ret;
    }

}