package Dictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileReader;

public class DictionaryManagement extends Dictionary {
    private String cd;

    public DictionaryManagement() {
        cd = System.getProperty("user.dir") + "/src/main/resources/";
        File file = new File(cd + "database/database.txt");
        String item;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((item = br.readLine()) != null) {
                database.push(item);
            }
        }catch(Exception exception) {
            System.err.println("Cannot open database.txt");
        }
    }

    public void createAllHTML() {
        //pullDataFromServer();
        pullDataFromTxt();
        try {
            for (int i = 0; i < n; i++) {
                boolean flag = false;
                String fileName = "";
                for (int j = 0; j < name[i].length(); j++) {
                    if (Character.compare(name[i].charAt(j), ' ') == 0) {
                        break;
                    }
                    fileName += name[i].charAt(j);
                }
                for (int j = 0; j < fileName.length(); j++) {
                    char c = fileName.charAt(j);
                    if (!( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ))
                        flag = true;
                }
                if (flag)
                    continue;

                StringBuilder htmlStringBuilder = new StringBuilder();
                htmlStringBuilder.append("<html>\n<head>\n<title>");
                htmlStringBuilder.append(fileName);
                htmlStringBuilder.append("</title>\n</head>\n");
                htmlStringBuilder.append("<body>\n");

                htmlStringBuilder.append("<h1>");
                htmlStringBuilder.append(name[i]);
                htmlStringBuilder.append("</h1>\n");

                String []parts = description[i].split("\n");
                for (int j = 0; j < parts.length; j++) {
                    //@aback /ə'bæk/
                    if (parts[j] == null || parts[j].length() < 1) continue;
                    if (Character.compare(parts[j].charAt(0), '-') == 0) {
                        htmlStringBuilder.append("<h4>");
                        htmlStringBuilder.append(parts[j]);
                        htmlStringBuilder.append("</h4>");
                        htmlStringBuilder.append("\n");
                    }
                    else if (Character.compare(parts[j].charAt(0), '=') == 0) {
                        htmlStringBuilder.append("<h4>");
                        htmlStringBuilder.append(parts[j]);
                        htmlStringBuilder.append("</h4>");
                        htmlStringBuilder.append("\n");
                    }
                    else if (Character.compare(parts[j].charAt(0), '*') == 0) {
                        htmlStringBuilder.append("<h2>");
                        htmlStringBuilder.append(parts[j]);
                        htmlStringBuilder.append("</h2>");
                        htmlStringBuilder.append("\n");
                    }

                }
                htmlStringBuilder.append("</body>\n</html>");
                WriteToFile(htmlStringBuilder.toString(),   fileName + ".html");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteToFile(String fileContent, String fileName) throws IOException {
        String projectPath = System.getProperty("user.dir")+ "/src/main/resources/database/file/";
        String tempFile = projectPath + fileName;
        File file = new File(tempFile);
        if (file.exists()) {
            try {
                File newFileName = new File(projectPath + File.separator + "backup_" + fileName);
                file.renameTo(newFileName);
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(file.getAbsoluteFile());
        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
        Writer writer = new OutputStreamWriter(outputStream);
        writer.write(fileContent);
        writer.close();
    }

    public static void main(String[] args) {
        DictionaryManagement oxford = new DictionaryManagement();
        oxford.createAllHTML();
    }
}