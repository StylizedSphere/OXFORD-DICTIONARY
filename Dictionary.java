public class Dictionary {
    protected Word[] data;
    protected int n;
    protected static final int TOTAL_QUERY = 1000;
    public Dictionary() {
        data = new Word[TOTAL_QUERY];
        n = 0;
    }
}
