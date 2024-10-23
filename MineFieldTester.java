public class MineFieldTester {
    public static void main(String[] args) {
        boolean[][] mine = new boolean[10][10];
        MineField m = new MineField(mine);
        System.out.println(m.toString());

    }
}
