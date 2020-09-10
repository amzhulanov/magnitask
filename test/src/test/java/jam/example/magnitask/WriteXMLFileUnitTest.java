package jam.example.magnitask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестирую метод создания XML-файла
 */

public class WriteXMLFileUnitTest {

    private final List<Integer> listN=new ArrayList<>();

    @BeforeEach
    void prepareData() {
        for (int i = 0; i < 25; i++) {
            listN.add(i+1);
        }
    }

    @Test
    void writeFileTest() {
        WriteXMLFile writeXMLFile;
        writeXMLFile = new WriteXMLFile(new File("src/test/resources/test.xml"), listN);
        writeXMLFile.writeFile();
        File xmlFile=new File("src/test/resources/test.xml");
        assertTrue(xmlFile.exists());
    }
}
