public class MineFieldTester {

    public static MineField mineField;
    public static void main(String[] args) {

        // Define the size of the minefield
        int rows = 5;
        int cols = 5;

        // Create a 2D array representing the minefield
        boolean[][] minefield = new boolean[rows][cols];

        // Place mines in the field (for example purposes)
        minefield[1][1] = true;
        minefield[2][3] = true;
        minefield[4][0] = true;
        MineField mf = new MineField(minefield);
        System.out.println(mf.toString());

        // testNumAdjacentMines(mineField);
    }

    private static void testNumAdjacentMines(MineField mineField) {
        mineField = new MineField(10, 10, 50);
        mineField.populateMineField(0,0);
        System.out.println(mineField.toString());
        // int res = mineField.countAdjacentMinesFromMiddle(1, 1);
        int res = mineField.numAdjacentMines(0, 0);
        System.out.println(res);
    }

    private static void testPopulateMineField() {
        mineField = new MineField(100, 100, 43);
        mineField.populateMineField(1, 1);
        System.out.println(mineField.toString());
        System.out.println(countMines(mineField));
        // assert countMines(mineField);
        
    }
    private static boolean countMines(MineField mineField) {
        int rows = mineField.numRows();
        int cols = mineField.numCols();
        int mineCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mineField.hasMine(i, j)) {
                    mineCount++;
                }
            }
        }
        return mineCount == mineField.numMines();
    }


}
