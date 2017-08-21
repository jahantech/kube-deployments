
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class httpListener {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/status", new serviceStatus());
        server.createContext("/getdata", new getData());
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

}
