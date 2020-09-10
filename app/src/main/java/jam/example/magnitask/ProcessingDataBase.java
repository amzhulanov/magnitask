package jam.example.magnitask;

import java.io.File;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных
 */
public class ProcessingDataBase implements Serializable {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Test(FIELD INTEGER)";
    private static final String CHECK_DATA = "SELECT * FROM TEST LIMIT 1";
    private static final String DELETE_DATA = "DELETE FROM TEST";
    private static final String INSERT_DATA = "INSERT INTO test(field) VALUES(?);";
    private static final String READ_DATA = "SELECT * FROM TEST";

    private Connection conn;
    private ResultSet rs;
    private Statement stmt;

    private String[] args;
    private File sourceFileXML;


    public ProcessingDataBase(Connection conn, String[] args, File sourceFileXML) {
        this.conn = conn;
        this.sourceFileXML = sourceFileXML;
        this.args = args;
    }

    /**
     * метод для наполнения таблицы Test
     * - создает таблицу Test, если её не существует
     * - Удаляет записи при их наличии
     * - вставляет новые данные
     *
     * @throws SQLException
     */
    public void dataCUD() throws SQLException {
        createTable();
        deleteField();
        insertRecord();
    }

    /**
     * Метод создаёт пакет с данными и загружает их в базу
     */
    private void insertRecord() {
        final PreparedStatement ps;
        try {
            ps = conn.prepareStatement(INSERT_DATA);
            for (int i = 1; i <= Integer.parseInt(args[2]); i++) {
                ps.setInt(1, i);
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Метод проверяет наличие таблицы Test и создаёт ей, при отсутствии
     *
     * @throws SQLException
     */
    private void createTable() throws SQLException {
        stmt = conn.createStatement();
        stmt.execute(CREATE_TABLE);
    }

    /**
     * Метод удаляет все записи из таблицы Test, если они есть
     *
     * @throws SQLException
     */
    public void deleteField() throws SQLException {
        stmt = conn.createStatement();
        rs = stmt.executeQuery(CHECK_DATA);
        if (rs.next()) {
            stmt.execute(DELETE_DATA);
        }
    }

    /**
     * Закрываю подключение
     */    public void closeConn() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * метод считывает данные из таблицы
     * обращается к модулю xmlTransform для записи данных в XML файл
     */
    public void readAndWriteData() {
        List<Integer> listN = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(READ_DATA);

            while (rs.next()) {
                listN.add(rs.getInt("field"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        WriteXMLFile writeXMLFile = new WriteXMLFile(sourceFileXML, listN);
        writeXMLFile.writeFile();
    }

}
