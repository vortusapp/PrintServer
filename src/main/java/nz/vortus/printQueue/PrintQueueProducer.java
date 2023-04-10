package nz.vortus.printQueue;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import static nz.vortus.ConfigProperties.getKafkaPrintQueueTopic;
import static nz.vortus.printQueue.DocType.PAPER;

public class PrintQueueProducer {

    private static final String TOPIC_NAME = getKafkaPrintQueueTopic();

    public static void main(String[] args) throws IOException {

    }
    public static void PrintPDF (URL url, DocType docType) throws IOException {
        InputStream in = url.openStream();
        Path tempFile = Files.createTempFile("prefix-", ".suffix");
        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        in.close();
        System.out.println("Downloaded file saved to: " + tempFile);
        PrintPDF(tempFile.toString(), docType);
    }

    public static void PrintPDF (String path, DocType docType) throws IOException {


        File pdfFile = new File(path);
        if (!pdfFile.exists()) {
            System.out.println("File not found: " + path);
            System.exit(1);
        }

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        Producer<String, byte[]> producer = new KafkaProducer<>(props);

        try (FileInputStream inputStream = new FileInputStream(pdfFile)) {
            byte[] pdfBytes = new byte[(int) pdfFile.length()];
            inputStream.read(pdfBytes);
            PrintJob printJob = new PrintJob(docType.name(), pdfBytes);
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC_NAME, printJob.getDocType(), printJob.getDocBytes());
            producer.send(record);
        } catch (IOException e) {
            e.printStackTrace();
        }

        producer.close();
    }
}

