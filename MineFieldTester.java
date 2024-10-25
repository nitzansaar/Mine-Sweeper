public class MineFieldTester {

    public static MineField mineField;
    public static void main(String[] args) {
        // MineField mineField = new MineField(10, 10, 3);
        // System.out.println(mineField.toString());
        // System.out.println(mineField.numMines());
        // mineField.populateMineField(1, 1);
        // System.out.println(mineField.toString());
        // testPopulateMineField();
        testNumAdjacentMines(mineField);
    }

    private static void testNumAdjacentMines(MineField mineField) {
        mineField = new MineField(10, 10, 50);
        mineField.populateMineField(1,1);
        System.out.println(mineField.toString());
        // int res = mineField.countAdjacentMinesFromMiddle(1, 1);
        int res = mineField.numAdjacentMines(1, 1);
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
