package org.example;

import java.awt.print.PrinterException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws PrinterException, MalformedURLException {
       new PDFPrinter(new URL("https://www.africau.edu/images/default/sample.pdf"));
    }
}