package jam.example.magnitask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WriteXMLFileUnitTest {

    private WriteXMLFile writeXMLFile;
    private List<Integer> listN=new ArrayList<>();

    @BeforeEach
    private void prepareData() {
        for (int i = 0; i < 25; i++) {
            listN.add(i+1);
        }

    }

    @Test
    public void writeFileTest() {
        writeXMLFile = new WriteXMLFile(new File("src/test/resources/test.xml"), listN);
        writeXMLFile.writeFile();
        File xmlFile=new File("src/test/resources/test.xml");
        assertTrue(xmlFile.exists());
    }
}
