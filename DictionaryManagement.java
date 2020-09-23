import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DictionaryManagement extends Dictionary{
    public void insertFromCommandLine(String wordTarget, String wordExplain) {
        if (n == TOTAL_QUERY - 1)
            throw new IndexOutOfBoundsException("Database is already full!");
        else
            data[n++] = new Word(wordTarget, wordExplain);
    }

    public void insertFromFile(String databaseName) {
        try {
            File database = new File(databaseName);
            Scanner reader = new Scanner(database);
            while (reader.hasNextLine()) {
                String wordTarget = reader.nextLine();
                String wordExplain = reader.nextLine();
                insertFromCommandLine(wordTarget, wordExplain);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading database.");
            e.printStackTrace();
        }
    }

    public String dictionaryLookup(String wordTarget) {
        for (int i = 0; i < n; i++) {
            if (data[i].getWordTarget().equals(wordTarget)) {
                return(data[i].getWordExplain());
            }
        }
        return null;
    }

    public void deleteWord(String word) {
        for (int i = 0; i < n; i++) {
            if (data[i].getWordTarget().equals(word)) {
                data[i] = data[n - 1];
                n--;
                return;
            }
        }
        System.out.println("There is no such word in dictionary");
    }

    public void showAllWords() {
        for (int i = 0; i < n; i++) {
            System.out.println(data[i].getWordTarget() + "\t" +data[i].getWordExplain());
        }
    }

    public static void main(String[] args) {
        DictionaryManagement oxford = new DictionaryManagement();
        oxford.insertFromCommandLine("lion", "cute and friendly animal, lives in African");
        oxford.insertFromFile("database.txt");
        oxford.showAllWords();
        oxford.dictionaryLookup("lion");
    }
}
