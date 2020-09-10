package jam.example.magmitask;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Класс для расчёта арифметической суммы чисел, указанных в XML файле
 */
public class ArithmeticFromXML implements Serializable {

    private final File file;
    private long sum = 0L;

    /**
     * @param file - имя XML файла, в котором находятся числа для обработки
     *
     */
    public ArithmeticFromXML(File file) {
        this.file = file;
    }

    /**
     *Метод считывает данные из XML файла
     * @return Возвращает арифметическую сумму всех значений
     * @throws FileNotFoundException - исключение, если XML-файл не найден
     * @throws XMLStreamException
     */
    public Long arithmetic() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try{ XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(file));
        while (reader.hasNext()) {
            XMLEvent xmlEvent = reader.nextEvent();
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("entry")) {
                    arithmeticSum(Long.parseLong(startElement.getAttributeByName(new QName("field")).getValue()));
                }
            }
        }} catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }

        return sum;
    }

    /**
     * Метод выполняет сложение элементов, полученных из XML-файла
     * @param item - элемент из поля field
     */
    private void arithmeticSum(Long item){
        sum+=item;
    }

}
