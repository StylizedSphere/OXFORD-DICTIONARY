import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dictionary {
    protected ArrayStack<String> database;
    protected String[] name;
    protected String[] description;
    protected int n;
    protected static final int TOTAL_QUERY = 1000000;

    public Dictionary() {
        n = 0;
        name = new String[TOTAL_QUERY];
        description = new String[TOTAL_QUERY];
        database = new ArrayStack<String>();
    }

    protected void pullData() {
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/translate",
                                   "postgres", "645hoanghoatham");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM DICTIONARY;" );
            while ( rs.next() ) {
                String s = rs.getString("name");
                name[n] = s.substring(1, s.length());
                description[n] = rs.getString("description");

                String fileName = "";
                for (int j = 0; j < name[n].length(); j++) {
                    if (Character.compare(name[n].charAt(j), ' ') == 0) {
                        break;
                    }
                    fileName += name[n].charAt(j);
                }
                Files.write(Paths.get("E:\\PERSONAL\\JAVA OOP\\src\\main\\resources\\database\\database.txt"), (fileName + "\n").getBytes(), StandardOpenOption.APPEND);
                n++;
            }
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static void main(String[] args) {
        Dictionary oxford = new Dictionary();
        oxford.pullData();

    }
}