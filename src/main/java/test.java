import java.io.File;
import java.io.FileWriter;

public class test {
    public static void main(String[] args) {
        File file = new File("E:\\PERSONAL\\JAVA OOP\\src\\main\\resources\\database\\database.txt");
        try {
            FileWriter w = new FileWriter(file);
            w.write("\nbob");
            w.close();
        }catch(Exception exception) {
            System.err.println("Cannot open database.txt");
        }
    }
}
// apple
// a


// ap
