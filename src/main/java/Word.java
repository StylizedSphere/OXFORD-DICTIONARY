import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class Word {
    public static void pushData(String name, String des) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/translate",
                            "postgres", "645hoanghoatham");

            String query = "INSERT INTO DICTIONARY(NAME,DESCRIPTION) VALUES (?, ?)";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, des);
            ps.addBatch();

            ps.executeBatch();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
    // push data into server
    // get all names to database.txt

}