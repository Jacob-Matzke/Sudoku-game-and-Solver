public class Pair<K, V> {

    K x;
    V y;

    public Pair(K x, V y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
