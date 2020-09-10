package jam.example.magnitask;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    private static String url;
    private static String sourceFileXML;
    private static String resultFileXML;
    private static String driverName;

    public static void main(String[] args) throws SQLException, TransformerException {
        configApp();

        ProcessingDataBase processingDataBase = new ProcessingDataBase(connectDB(args[0], args[1]), args, new File(sourceFileXML));
        processingDataBase.dataCUD();
        processingDataBase.readAndWriteData();
        processingDataBase.closeConn();

        XslTransformer xslTransformer = new XslTransformer(new File(sourceFileXML), new File(resultFileXML));
        xslTransformer.Transformer();

        ArithmeticFromXML arithmeticFromXML = new ArithmeticFromXML(new File(resultFileXML));
        System.out.printf("Arithmetic sum %d %n", arithmeticFromXML.arithmetic());
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

        ResourceBundle props = ResourceBundle.getBundle("config");
        driverName = props.getString("driver.name");
        sourceFileXML = props.getString("sourceFileXML");
        resultFileXML = props.getString("resultFileXML");
        url = props.getString("url");
    }

}