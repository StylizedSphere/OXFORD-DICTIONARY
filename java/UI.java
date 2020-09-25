import javax.swing.JFrame;
public abstract class UI extends JFrame{
    protected String cd;
    protected DictionaryManagement oxford;
    protected UI() {
        initUI();
        oxford = new DictionaryManagement();
        cd = System.getProperty("user.dir") + "/src/main/resources/";
        oxford.insertFromFile(cd + "database/database.txt");
    }
    public abstract void initUI();
}
