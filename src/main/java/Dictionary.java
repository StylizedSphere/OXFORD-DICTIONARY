public class Dictionary {
    protected String[] data;
    protected int n;
    protected static final int TOTAL_QUERY = 1000;
    public Dictionary() {
        data = new String[TOTAL_QUERY];
        n = 0;
    }
}
