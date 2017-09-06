
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class httpListener {
    numReq=0;
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/status", new serviceStatus());
        server.createContext("/getdata", new getData());
        server.createContext("/gethostname", new getHostName());
        server.createContext("/metrics", new getMetrics());
        server.start();
    }

    static class serviceStatus implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Status:OK";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    static class getData implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Need to get data from redis";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    static class getData implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            int response = numReq;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    static class getHostName implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
		String hostname = "None";
                try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		    String response = hostname;
		    t.sendResponseHeaders(200, response.length());
		    OutputStream os = t.getResponseBody();
		    os.write(response.getBytes());
		    os.close();
		    numReq = numReq + 1;
		}
		catch (UnknownHostException ex)
		{
		    String response = "unable to find hostname";
		    t.sendResponseHeaders(200, response.length());
		    OutputStream os = t.getResponseBody();
		    os.write(response.getBytes());
		    os.close();
		}
        }
    }

}
