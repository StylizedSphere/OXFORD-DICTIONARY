package Dictionary;

import javax.swing.JFrame;
public abstract class UI extends JFrame{
    protected String cd;
    protected DictionaryManagement oxford;
    protected UI() {
        cd = System.getProperty("user.dir") + "/src/main/resources/";
        oxford = new DictionaryManagement();
        initUI();
    }
    public abstract void initUI();
}
