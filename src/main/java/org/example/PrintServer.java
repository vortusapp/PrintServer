package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PrintServer {
    private static final int PORT = 16335;
    private static final String API_KEY = "cheese";
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

abstract class AbstractPrintHandler implements HttpHandler {
    private final String apiKey;

    public AbstractPrintHandler(String apiKey) {
        this.apiKey = apiKey;
    }
}
class PrintFileHandler extends AbstractPrintHandler {
    private static final String API_KEY = "cheese";

    public PrintFileHandler(String apiKey) {
        super(apiKey);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        checkAPIKey(exchange);
        String filePath = new String(exchange.getRequestBody().readAllBytes());
        try {
            PDFPrinter pdfPrinter = new PDFPrinter(new File(filePath));
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }
        String response = "File printed: " + filePath;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void checkAPIKey(HttpExchange exchange) throws IOException {
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-Key");
        if (!API_KEY.equals(apiKey)) {
            exchange.sendResponseHeaders(401, 0);
            exchange.close();
            return;
        }
    }
}

class PrintURLHandler extends AbstractPrintHandler {
    private static final String API_KEY = "cheese";

    public PrintURLHandler(String apiKey) {
        super(apiKey);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        checkAPIKey(exchange);
        String url = new String(exchange.getRequestBody().readAllBytes());
        try {
            PDFPrinter pdfPrinter = new PDFPrinter(new URL(url));
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }
        String response = "URL printed: " + url;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void checkAPIKey(HttpExchange exchange) throws IOException {
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-Key");
        if (!API_KEY.equals(apiKey)) {
            exchange.sendResponseHeaders(401, 0);
            exchange.close();

        }
    }
}



