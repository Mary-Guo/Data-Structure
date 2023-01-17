public class OffByN implements CharacterComparator {
    private int diff;
    public OffByN(int N) {
        diff = N;
    }
    public boolean equalChars(char x, char y) {
        int a = x;
        int b = y;
        if ((Math.abs(a - b) == diff)) {
            return true;
        }
        return false;
    }
}
