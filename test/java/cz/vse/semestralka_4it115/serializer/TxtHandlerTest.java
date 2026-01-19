// File: serializer.TxtHandlerTest.java
package cz.vse.semestralka_4it115.serializer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class TxtHandlerTest {

    @Test
    void testLoadValidTxtFile() throws IOException {
        // Create a temporary text file
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile, java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write("Hello\nWorld");
        }

        TxtHandler handler = new TxtHandler();
        String content = handler.loadTXT(tempFile.getAbsolutePath());
        assertTrue(content.contains("Hello"));
        assertTrue(content.contains("World"));
    }

    @Test
    void testLoadNonexistentFileReturnsErrorMessage() {
        TxtHandler handler = new TxtHandler();
        String result = handler.loadTXT("nonexistent_file.txt");
        assertTrue(result.toLowerCase().contains("nepodařilo se načíst"));
    }
}
