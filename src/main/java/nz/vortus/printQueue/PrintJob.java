package nz.vortus.printQueue;

class PrintJob {
    private final String docType;
    private final byte[] docBytes;

    public PrintJob(String docType, byte[] docBytes) {
        this.docType = docType;
        this.docBytes = docBytes;
    }

    public String getDocType() {
        return docType;
    }

    public byte[] getDocBytes() {
        return docBytes;
    }
}
