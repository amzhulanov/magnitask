package jam.example.magnitask;

import jam.example.magmitask.ArithmeticFromXML;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArithmeticFromXMLUnitTest {
    final String fileName = "src/main/resources/2.xml";

    ArithmeticFromXML arithmeticFromXML;

    @Test
    public void getSum()  {
        arithmeticFromXML=new ArithmeticFromXML(new File(fileName));
        Long sum=arithmeticFromXML.arithmetic();
        assertNotNull(sum);
        assertTrue(sum==500000500000L);
    }


}
