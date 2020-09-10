package jam.example.magnitask;

import jam.example.magmitask.ArithmeticFromXML;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;


/**
 * Основной класс программы.
 * Поочереди обращается к классам для
 * -записи данных в таблицу
 * -выгрузки данных в xml
 * -преобразования данных через xlst
 * -считывания данных из xml файла и рассчёта арифметической суммы
 *
 * При запуске приложения необходимо указать параметры: логин, пароль, количество записей
 * @author JAM
 */

public class MainApp {
    private static final String CFG = "app/src/main/resources/config.properties";
    private static String url;
    private static String sourceFileXML;
    private static String resultFileXML;
    private static String sourceFileXSLT;
    private static String driverName;

    public static void main(String[] args) throws SQLException, TransformerException, IOException, XMLStreamException {
        configApp();

        ProcessingDataBase processingDataBase = new ProcessingDataBase(connectDB(args[0], args[1]), args, new File(sourceFileXML));
        processingDataBase.dataCUD();
        processingDataBase.readAndWriteData();
        processingDataBase.closeConn();

        XslTransformer xslTransformer = new XslTransformer(new File(sourceFileXML), new File(resultFileXML),new File(sourceFileXSLT));
        xslTransformer.Transformer();

        ArithmeticFromXML arithmeticFromXML = new ArithmeticFromXML(new File(resultFileXML));
        System.out.printf("Арифметическая сумма %d %n", arithmeticFromXML.arithmetic());
    }

    /**
     * Метод для подключения к БД
     * @param user логин
     * @param pwd пароль
     * @return Возвращает установленное подключение
     * @throws SQLException
     */
    private static Connection connectDB(String user, String pwd) throws SQLException {
        try {

            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        return DriverManager.getConnection(url, user, pwd);
    }

    /**
     * Метод для загрузки параметров
     */
    private  static void configApp() {
        FileInputStream fis=null;
        Properties property = new Properties();
        try {
            fis = new FileInputStream(CFG);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            property.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverName = property.getProperty("driver.name");
        sourceFileXML = property.getProperty("sourceFileXML");
        resultFileXML = property.getProperty("resultFileXML");
        sourceFileXSLT = property.getProperty("sourceFileXSLT");

        url = property.getProperty("url");
    }
}