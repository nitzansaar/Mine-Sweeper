public class VisibleFieldTester {
    private MineField mineField;
    private VisibleField visibleField;

    public static void main(String[] args) {
        VisibleFieldTester test = new VisibleFieldTester();
        
        test.setUp();
        test.testInitialStatus();
        test.testCycleGuess();
        test.testUncoverSafeSquare();
        test.testWinCondition();
        test.testResetGameDisplay();

        System.out.println("All tests passed.");
    }

    public void setUp() {
        mineField = new MineField(10, 10, 3);
        mineField.populateMineField(0, 0);
        visibleField = new VisibleField(mineField);
        
    }

    public void testInitialStatus() {

        for (int row = 0; row < mineField.numRows(); row++) {
            for (int col = 0; col < mineField.numCols(); col++) {
                assert visibleField.getStatus(row, col) == VisibleField.COVERED : "Initial status should be COVERED";
            }
        }
        System.out.println(mineField.toString());
        // System.out.println(visibleField.numMinesLeft());
        System.out.println(mineField.numMines());
        assert mineField.numMines() == 3 : "Initial mines left should equal the number of mines in the field";
        assert !visibleField.isGameOver() : "Game should not be over at the start";
    }

    public void testCycleGuess() {
        visibleField.cycleGuess(0, 0);
        assert visibleField.getStatus(0, 0) == VisibleField.MINE_GUESS : "First cycle should set to MINE_GUESS";
        assert visibleField.numMinesLeft() == 2 : "Mines left should decrease when setting a guess";

        visibleField.cycleGuess(0, 0);
        assert visibleField.getStatus(0, 0) == VisibleField.QUESTION : "Second cycle should set to QUESTION";

        visibleField.cycleGuess(0, 0);
        assert visibleField.getStatus(0, 0) == VisibleField.COVERED : "Third cycle should set back to COVERED";
        assert visibleField.numMinesLeft() == 3 : "Mines left should return to original count after cycling to COVERED";
    }

    public void testUncoverSafeSquare() {
        assert visibleField.uncover(0, 0) : "Uncovering a safe square should return true";
        assert visibleField.getStatus(0, 0) == 0 : "Safe square status should show adjacent mine count";
    }

    public void testWinCondition() {
        // Uncover all non-mine squares
        visibleField.uncover(0, 0);
        visibleField.uncover(0, 2);
        visibleField.uncover(1, 0);
        visibleField.uncover(1, 1);
        visibleField.uncover(2, 1);
        visibleField.uncover(2, 2);

        assert visibleField.isGameOver() : "Game should be over after uncovering all non-mine squares";
    }

    public void testResetGameDisplay() {
        visibleField.uncover(0, 0);
        visibleField.cycleGuess(0, 1);
        visibleField.resetGameDisplay();

        // Check all squares are reset to COVERED
        for (int row = 0; row < mineField.numRows(); row++) {
            for (int col = 0; col < mineField.numCols(); col++) {
                assert visibleField.getStatus(row, col) == VisibleField.COVERED : "Status should be reset to COVERED";
            }
        }
        assert !visibleField.isGameOver() : "Game should not be over after reset";
        assert visibleField.numMinesLeft() == 3 : "Mines left should reset to original count after reset";
    }
}
