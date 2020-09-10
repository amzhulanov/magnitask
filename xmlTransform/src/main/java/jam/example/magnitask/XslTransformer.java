package jam.example.magnitask;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.Serializable;

/**
 * Класс выполняет преобразование XML посредством XSLT
 */
public class XslTransformer implements Serializable {
    //private final String SOURCE_FILE_XSLT="xmlTransform/src/main/resources/default.xslt";
    private final File sourceFileXSLT;
    private final File sourceFileXML;
    private final File resultFileXML;

    /**
     * @param sourceFileXML - файл XML источник
     * @param resultFileXML - XML файл для сохранения изменений
     */
    public XslTransformer(File sourceFileXML, File resultFileXML,File sourceFileXSLT) {
        this.sourceFileXML = sourceFileXML;
        this.resultFileXML = resultFileXML;
        this.sourceFileXSLT=sourceFileXSLT;
    }

    /**
     * Метод использует ст андартную библиотеку для XSLT преобразования XML файла
     * @throws TransformerException
     */
    public void Transformer() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(sourceFileXSLT);
        Transformer transformer = factory.newTransformer(xslt);
        Source xml = new StreamSource(sourceFileXML);
        transformer.transform(xml, new StreamResult(resultFileXML));
    }

}
