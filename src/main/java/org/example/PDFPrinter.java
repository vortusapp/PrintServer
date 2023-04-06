package org.example;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
        import java.io.File;
        import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.printing.PDFPageable;

public  class PDFPrinter {

    private  File file = null;
    private  String pathname ;
    private URL url;

    PDFPrinter(File file) throws PrinterException {

        this.file = file;
        print();
    }
    PDFPrinter(URL url) throws PrinterException {
        this.url = url;
        print();
    }
        void print(){

            try (PDDocument document = file != null ? PDDocument.load(file) : PDDocument.load(url.openStream())) {

                SetPaperSize(document);
                PrinterJob job = PrinterJob.getPrinterJob();
                SetToPageable(document, job);
                job.print();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (PrinterException e) {
                throw new RuntimeException(e);
            }
        }

    private static void SetToPageable(PDDocument document, PrinterJob job) {
        job.setPageable(new PDFPageable(document));
    }

    private static void SetPaperSize(PDDocument document) {
        PDRectangle pageSize = PDRectangle.A4;
        for (PDPage page : document.getPages()) {
            page.setMediaBox(pageSize);
        }
    }
}

