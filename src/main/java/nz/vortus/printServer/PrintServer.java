package nz.vortus.printServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static nz.vortus.ConfigProperties.getServerAPIKey;
import static nz.vortus.ConfigProperties.getServerPort;

public class PrintServer {
    private static final int PORT = getServerPort();
    private static final String API_KEY = getServerAPIKey();
    private static final Map<String, HttpHandler> HANDLERS = new HashMap<>();

    static {
        HANDLERS.put("/printFile", new PrintFileHandler(API_KEY));
        HANDLERS.put("/printURL", new PrintURLHandler(API_KEY));
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        for (String path : HANDLERS.keySet()) {
            server.createContext(path, HANDLERS.get(path));
        }
        server.start();
    }
}




