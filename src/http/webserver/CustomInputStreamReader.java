package http.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Custom InputStreamReader used because the default one was blocking at the end of the stream
 * when there was not and end-of-line character.
 */
public class CustomInputStreamReader extends InputStreamReader{


    /**
     * Default constructor for our custom InputStreamReader
     * @param stream : an InputStream
     */
    public CustomInputStreamReader(InputStream stream){
        super(stream);
    }

    /**
     * Overrides the default read by adding an end-of-line character at the end of the stream.
     */
    @Override
    public int read() throws IOException {
        int ret = super.read();

        if(ret == -1){
            return '\n';
        }
        else{
            return ret;
        }

    }

    /**
     * Overrides the default read by adding an end-of-line character at the end of the stream.
     */
    @Override
    public int read(char[] cbuf, int offset, int length) throws IOException {
        
        int ret = super.read(cbuf, offset, length);

        if(ret > 0 && ret <= length - 1){
            cbuf[ret] = '\n';
        }

        return ret;

    }

}