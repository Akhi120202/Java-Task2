/**
 * DataExchangeSimulation class simulates data exchange using byte and character streams.
 * It demonstrates writing and reading messages to/from files using both stream types.
 * 
 */

import java.io.*;

public class DataExchangeSimulation {
    public void simulateDataExchange() {
        String message = "Hello, this is a data exchange simulation.";

        // Simulate data exchange using byte streams
        simulateByteStreamExample(message);

        // Simulate data exchange using character streams
        simulateCharacterStreamExample(message);
    }

    public String simulateByteStreamExample(String message) {
        String fileName = "byteDataStreamExample.txt";

        // Writing to a file using byte stream (FileOutputStream)
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] data = message.getBytes(); // Convert the string to bytes
            outputStream.write(data);
            System.out.println("Data written to file using byte stream.");
        } catch (IOException e) {
            e.printStackTrace();
            return "Error writing data using byte stream.";
        }

        // Reading from a file using byte stream (FileInputStream)
        try (InputStream inputStream = new FileInputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String readMessage = new String(buffer, 0, bytesRead);
            System.out.println("Data read from file using byte stream: " + readMessage);
            return readMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading data using byte stream.";
        }
    }

    public String simulateCharacterStreamExample(String message) {
        String fileName = "characterDataStreamExample.txt";

        // Writing to a file using character stream (FileWriter)
        try (Writer writer = new FileWriter(fileName)) {
            writer.write(message);
            System.out.println("Data written to file using character stream.");
        } catch (IOException e) {
            e.printStackTrace();
            return "Error writing data using character stream.";
        }

        // Reading from a file using character stream (FileReader)
        try (Reader reader = new FileReader(fileName)) {
            char[] buffer = new char[1024];
            int charsRead = reader.read(buffer);
            String readMessage = new String(buffer, 0, charsRead);
            System.out.println("Data read from file using character stream: " + readMessage);
            return readMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading data using character stream.";
        }
    }
}
