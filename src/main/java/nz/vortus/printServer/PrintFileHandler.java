package nz.vortus.printServer;

import com.sun.net.httpserver.HttpExchange;
import nz.vortus.PDFPrinter;
import nz.vortus.printQueue.DocType;
import nz.vortus.printQueue.PrintQueueProducer;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

class PrintFileHandler extends AbstractPrintHandler {
    private static final String API_KEY = "cheese";

    public PrintFileHandler(String apiKey) {
        super(apiKey);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        checkAPIKey(exchange);
        String filePath = new String(exchange.getRequestBody().readAllBytes());
        PrintQueueProducer.PrintPDF(filePath, DocType.PAPER);
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

        }
    }
}