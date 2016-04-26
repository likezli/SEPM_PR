package sepm.ss16.e0828454.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler {

    private static Connection conn = null;
    private static final Logger logger = LogManager.getLogger(DBHandler.class);

    public static void openConnection() throws SQLException {


        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            logger.info("org.h2.Driver not found");
        }

        conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        conn.setAutoCommit(false);

    }

    public static Connection getConnection() throws SQLException{
        if (conn == null || conn.isClosed()) {
            try {
                openConnection();
            } catch (SQLException e) {
                logger.error("Error: Failed to open Connection");
            }
        }

        return conn;
    }

    public static void closeConnection() throws SQLException  {
        try {
            if (conn != null || !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("Error: Failed to close Connection", e);
        }
    }

}
