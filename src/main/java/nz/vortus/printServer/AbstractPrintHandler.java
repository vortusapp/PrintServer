package nz.vortus.printServer;

import com.sun.net.httpserver.HttpHandler;

abstract class AbstractPrintHandler implements HttpHandler {
    private final String apiKey;

    public AbstractPrintHandler(String apiKey) {
        this.apiKey = apiKey;
    }
}