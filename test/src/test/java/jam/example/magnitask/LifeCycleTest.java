package jam.example.magnitask;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестирую весь цикл работы приложения и общее время выполнения
 * 1. Подключиться к БД и добавить N записей
 * 2. Выгрузить данные из БД и сохранить в XML
 * 3. Преобразовать XML через XSLT
 * 4. Считать данные из нового XML и проссуммировать значения
 *
 */
public class LifeCycleTest {

    private Connection conn;
    private ResultSet rs;
    private Statement stmt;
    private static final String CFG = "src/test/resources/config.properties";
    private static String url;
    private static String sourceFileXML;
    private static String resultFileXML;
    private static String driverName;
    private static String user;
    private static String pwd;
    private final Long n=25L;
    private final Long limitTime=300L;

    private ProcessingDataBase processingDataBase;

    @BeforeEach
    void connectDB() throws ClassNotFoundException, SQLException {
        configApp();
        Class.forName(driverName);
        conn=DriverManager.getConnection(url, user, pwd);
        processingDataBase = new ProcessingDataBase(conn, new String[] {user,pwd,n.toString()}, new File(sourceFileXML));
        stmt = conn.createStatement();
    }

    @AfterEach
    void closeConnect() throws SQLException {
        processingDataBase.closeConn();
        assertTrue(conn.isClosed());
    }

    /**
     * 1. Подключиться к БД и добавить N записей
     * 2. Выгрузить данные из БД и сохранить в XML
     * 3. Преобразовать XML через XSLT
     * @throws SQLException
     */
    @Test
    void lifeCycleTest() throws SQLException  {
        Instant start = Instant.now();
        //processingDataBase = new ProcessingDataBase(conn, new String[] {user,pwd,n.toString()}, new File(sourceFileXML));
//1. Подключиться к БД и добавить N записей
        processingDataBase.dataCUD();
        rs = stmt.executeQuery("SELECT Count(field) FROM Test");
        assertTrue(rs.next());
        while (rs.next()){
            assertEquals(rs.getInt(1),n);
        }

//2. Выгрузить данные из БД и сохранить в XML
        processingDataBase.readAndWriteData();

//3. Преобразовать XML через XSLT
        XslTransformer xslTransformer=new XslTransformer(new File(sourceFileXML),new File(resultFileXML));
        try {
            xslTransformer.Transformer();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

//4. Считать данные из нового XML и проссуммировать значения
        ArithmeticFromXML arithmeticFromXML=new ArithmeticFromXML(new File(resultFileXML));
        Long sum=arithmeticFromXML.arithmetic();
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis()/ 1000;
        System.out.println(elapsed);



        assertThat (elapsed).isLessThan(limitTime);
        assertEquals((n + n* n)/2, (long) sum);
    }

    @Test
    void deleteDataTest() throws SQLException {
        processingDataBase.deleteField();
        rs = stmt.executeQuery("SELECT Count(field) FROM Test");
        while (rs.next()){
            assertEquals(rs.getInt(1),0);
        }
    }

    private  void configApp() {
        ResourceBundle props = ResourceBundle.getBundle("config");
        driverName = props.getString("driver.name");
        sourceFileXML = props.getString("sourceFileXML");
        resultFileXML = props.getString("resultFileXML");
        url = props.getString("url");
        user=props.getString("user");
        pwd= props.getString("pwd");
    }

}
