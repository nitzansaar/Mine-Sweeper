import java.util.Random;
// Name: Nitzan Saar
// USC NetID: 8106373693
// CS 455 PA3
// Fall 2024


/** 
   MineField
      Class with locations of mines for a minesweeper game.
      This class is mutable, because we sometimes need to change it once it's created.
      Mutators: populateMineField, resetEmpty
      Includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
   
   // <put instance variables here>
   private static Random random = new Random();
   private boolean[][] mineData;
   private int numMines;
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in
      the array such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice
      versa.  numMines() for this minefield will corresponds to the number of 'true' values in 
      mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
      this.mineData = deepCopy(mineData);
      this.numMines = calculateNumMines(mineData);
   }


   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a 
      MineField, numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      boolean[][] field = new boolean[numRows][numCols];
      for (int i = 0; i < numRows; i++) {
         for (int j = 0; j < numCols; j++) {
            field[i][j] = false;
         }
      }
      this.numMines = numMines;
      this.mineData = field;
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on
      the minefield, ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
      resetEmpty();
      // repopulate the mine fields
      int minesPlaced = 0;
      while (this.numMines() > minesPlaced) {
         int randX = random.nextInt(numRows());
         int randY = random.nextInt(numCols());
         if (!this.mineData[randX][randY] && !(randX == row && randY == col)) {
            this.mineData[randX][randY] = true;
            minesPlaced++;
         }
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or
      numCols().  Thus, after this call, the actual number of mines in the minefield does not match
      numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in at the 
      beginning of a game.
    */
   public void resetEmpty() {
      // remove the mines from the mine field
      for (int i = 0; i < numRows(); i++) {
         for (int j = 0; j < numCols(); j++) {
            this.mineData[i][j] = false;
         }
      }
   }

   
  /**
     Returns the number of mines adjacent to the specified location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      return numAdjacentMinesHelper(row, col, new boolean[numRows()][numCols()]);
  }
  
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      return row >= 0 && row < numRows() && col >= 0 && col < numCols();
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return this.mineData.length;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return this.mineData[0].length;    
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      return this.mineData[row][col];  
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg
      constructor, some of the time this value does not match the actual number of mines currently
      on the field.  See doc for that constructor, resetEmpty, and populateMineField for more
      details.
      @return number of mines
    */
   public int numMines() {
      return this.numMines;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
       for (boolean[] mineData1 : mineData) {
           for (int j = 0; j < mineData1.length; j++) {
               sb.append(mineData1[j] ? "*" : ".");
           }
           sb.append("\n");
       }
      return sb.toString();
   }

   
   // <put private methods here>
   // Helper method to create a deep copy of the 2D array
   private boolean[][] deepCopy(boolean[][] original) {
      boolean[][] copy = new boolean[original.length][];
      for (int i = 0; i < original.length; i++) {
         copy[i] = original[i].clone();  // Clone each row to ensure a deep copy
      }
      return copy;
   }
      // Helper method to calculate the number of mines
   private int calculateNumMines(boolean[][] mineData) {
      int count = 0;
      for (boolean[] row : mineData) {
         for (boolean cell : row) {
            if (cell) {
               count++;
            }
         }
      }
      return count;
   }
  // Helper method with a visited array to avoid re-visiting cells
  private int numAdjacentMinesHelper(int row, int col, boolean[][] visited) {
   if (!inRange(row, col) || visited[row][col]) {
       return 0;
   }

   // Mark this cell as visited
   visited[row][col] = true;
   int mineCount = 0;

   // Check all adjacent cells
   for (int i = -1; i <= 1; i++) {
       for (int j = -1; j <= 1; j++) {
           if (i == 0 && j == 0) continue;  // Skip the current cell
           int newRow = row + i;
           int newCol = col + j;
           if (inRange(newRow, newCol) && hasMine(newRow, newCol)) {
               mineCount++;
           }
       }
   }

   return mineCount;
}

 
}

