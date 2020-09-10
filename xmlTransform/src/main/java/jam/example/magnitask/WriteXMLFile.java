package jam.example.magnitask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Класс формирует и наполняет XML-файл
 */
public class WriteXMLFile implements Serializable {

    private Document doc;
    private Element rootElement;
    private final File file;
    private final List<Integer> listN;


    public WriteXMLFile(File file, List<Integer> listN) {
        this.file = file;
        this.listN = listN;
    }

    /**
     * Метод создает структуру XML-файла
     * вызывает методя для заполнения структуры
     * записывает в XML-файл лист объектов типа Integer
     */
    public void writeFile() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            rootElement = doc.createElement("entries");
            doc.appendChild(rootElement);
            listN.forEach(this::doSomething);

            Transformer transformer = null;
            transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод добавляет в структуру XML файла теги и целочисленное значение
     * @param n - значение для поля field
     */
    private void doSomething(Integer n) {
        Element entry = doc.createElement("entry");
        rootElement.appendChild(entry);
        Element field = doc.createElement("field");
        field.appendChild(doc.createTextNode(n.toString()));
        entry.appendChild(field);
    }

}
