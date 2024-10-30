// Name: Nitzan Saar
// USC NetID: 8106373693
// CS 455 PA3
// Fall 2024


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because
  it's what the user can see about the minefield). Client can call getStatus(row, col) for any 
  square.  It actually has data about the whole current state of the game, including the underlying
  mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), 
  isGameOver().  It also has mutators related to actions the player could do (resetGameDisplay(),
  cycleGuess(), uncover()), and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms the Model for the
  game application, whereas GameBoardPanel is the View and Controller in the MVC design pattern.  It
  contains the MineField that it's partially displaying.  That MineField can be accessed
  (or modified) from outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus values [0,8] mentioned in comments below) are the
   // possible states of one location (a "square") in the visible field (all are values that can be
   // returned by public method getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this opened square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already
                                          // (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of
                                                  // losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused
                                                 // you to lose)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   private MineField mineField;
   private int[][] fieldStatus;
   private int minesLeft;
   private boolean gameOver;


   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the locations covered, no mines guessed, and the game not
      over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
      this.mineField = mineField;
      this.fieldStatus = new int[mineField.numRows()][mineField.numCols()];
      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            fieldStatus[i][j] = COVERED;
         }
      }
      minesLeft = mineField.numMines();
      gameOver = false;
   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      for (int i = 0; i < this.mineField.numRows(); i++) {
         for (int j = 0; j < this.mineField.numCols(); j++) {
            fieldStatus[i][j] = COVERED;
         }
      }
      minesLeft = this.mineField.numMines();
      gameOver = false;
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return this.mineField;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the
            beginning of the class for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return fieldStatus[row][col];
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines
      guessed are correct or not.  Just gives the user an indication of how many more mines the user
      might want to guess.  This value will be negative if they have guessed more than the number of
      mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return minesLeft;
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on
      a COVERED square changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to
      QUESTION;  call on a QUESTION square changes it to COVERED again; call on an uncovered square
      has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      int guess = fieldStatus[row][col];
      if (guess == COVERED) {
         fieldStatus[row][col] = MINE_GUESS;
         minesLeft--;
      } else if (guess == MINE_GUESS) {
         fieldStatus[row][col] = QUESTION;
         minesLeft++;
      } else if (guess == QUESTION) {
         fieldStatus[row][col] = COVERED;
      }
      //  switch (guess) {
      //      case COVERED -> {
      //          fieldStatus[row][col] = MINE_GUESS;
      //          minesLeft--;
      //      }
      //      case MINE_GUESS -> {
      //          fieldStatus[row][col] = QUESTION;
      //          minesLeft++;
      //      }
      //      case QUESTION -> fieldStatus[row][col] = COVERED;
      //      default -> {
      //      }
      //  }
      checkWin();
      
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in the
      neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form (possibly along with
      parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      if (mineField.hasMine(row, col)) {
         handleLoss();
         fieldStatus[row][col] = EXPLODED_MINE;
         return false;
      }

      uncoverRecursive(row, col);
      if (checkWin()) {
         handleWin();
      }

      return true;
   }
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game has ended
    */
   public boolean isGameOver() {
      return gameOver;  
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      return fieldStatus[row][col] >= 0;
   }
   
 
   // <put private methods here>
   private void uncoverRecursive(int row, int col) {
      if (!mineField.inRange(row, col)  || isUncovered(row, col) || fieldStatus[row][col] == MINE_GUESS) {
         return;
      }
      int numAdjacentMines = mineField.numAdjacentMines(row, col);
      fieldStatus[row][col] = numAdjacentMines;
      if (numAdjacentMines == 0) {
         for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
               if (!(i == 0 && j == 0)) {
                  uncoverRecursive(row  + i, col + j);
               }
            }

         }
      }
   }

   private boolean checkWin() {
      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            if (!isUncovered(i, j) && !mineField.hasMine(i, j)) {
               return false;
            }
         }
      }
      return true;
   }

   private void  handleWin() {
      gameOver = true;
   }

   private void handleLoss() {
      gameOver = true;
      for (int i = 0; i < mineField.numRows(); i++) {
         for (int j = 0; j < mineField.numCols(); j++) {
            if (mineField.hasMine(i, j)) {
               if (fieldStatus[i][j] != MINE_GUESS) {
                  fieldStatus[i][j] = MINE;
               }
            } else if (fieldStatus[i][j] == MINE_GUESS) {
               fieldStatus[i][j] = INCORRECT_GUESS;
            }
         }
      }
   }
 
}
