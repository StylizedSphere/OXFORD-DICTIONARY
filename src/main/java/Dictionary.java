package Dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private String cd;

    public Dictionary() {
        n = 0;
        name = new String[TOTAL_QUERY];
        description = new String[TOTAL_QUERY];
        database = new ArrayStack<String>();
        cd = System.getProperty("user.dir") + "/src/main/resources/";
    }

    protected void pullDataFromServer() {
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
                Files.write(Paths.get(cd + "/database/database.txt"), (fileName + "\n").getBytes(), StandardOpenOption.APPEND);
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

    protected void pullDataFromTxt() {
        File file = new File(cd + "/database/OxfordDatabase.txt");
        String item;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String stringName = "";
            String stringDes = "";
            boolean flag = false;
            while ((item = br.readLine()) != null) {
                if (item.equals(""))
                    continue;

                if (Character.compare(item.charAt(0), '@') == 0) {
                    if (flag) {
                        String fileName = "";
                        for (int j = 1; j < stringName.length(); j++) {
                            if (Character.compare(stringName.charAt(j), ' ') == 0) {
                                break;
                            }
                            fileName += stringName.charAt(j);
                        }
                        name[n] = fileName;
                        description[n] = stringDes;
                        Files.write(Paths.get(cd + "/database/database.txt"), (name[n] + "\n").getBytes(), StandardOpenOption.APPEND);
                        System.out.println(name[n]);
                        n++;
                    }
                    stringName = "";
                    stringDes = "";
                    stringName += item;
                    flag = true;
                }
                else
                    stringDes += (item + "\n");
            }
        }catch(Exception exception) {
            System.err.println("Cannot open database.txt");
        }
    }

    public static void main(String[] args) {
        Dictionary oxford = new Dictionary();
        oxford.pullDataFromTxt();

    }
}