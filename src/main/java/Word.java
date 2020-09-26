import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class Word {
    public static void main(String args[]) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/translate",
                                   "postgres", "645hoanghoatham");
            stmt = c.createStatement();
            String sql = "INSERT INTO OXFORD(NAME,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3) " +
                    "VALUES('cat', 'a small animal with soft fur that people often keep as a pet. Cats catch and kill birds and mice.', 'a wild animal of the cat family','THEY LOVE CHEESES')";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}