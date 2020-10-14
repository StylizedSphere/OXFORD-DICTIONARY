package Dictionary;

import javax.swing.JFrame;
public abstract class UI extends JFrame{
    protected String cd;
    protected DictionaryManagement oxford;
    protected UI() {
        oxford = new DictionaryManagement();
        initUI();
        cd = System.getProperty("user.dir") + "/src/main/resources/";
    }
    public abstract void initUI();
}
