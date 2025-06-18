// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int[] heights,widths;
	private int maxHeight;

	private int prevMaxHeight;
	private boolean[][] prevGrid;
	private int[] prevWidths,prevHeights;

	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;

		widths = new int[height];
		heights = new int[width];

		Arrays.fill(widths,0);
		Arrays.fill(heights,0);
		maxHeight = 0;

		prevMaxHeight = 0;
		prevGrid = new boolean[width][height];
		prevHeights = new int[width];
		prevWidths = new int[height];
		

	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			for(int i=0; i< grid[0].length; i++){
				int currentWidth=0;
				for(int j=0; j< grid.length; j++){
					if(grid[j][i]) currentWidth++;
				}
				if(currentWidth!=getRowWidth(i))
					throw new RuntimeException("There's a mistake in widths array");
			}
			int maxOne=0;
			for(int i=0; i< grid.length;i++){
				int maxCurrHeight=0;
				for(int j=0; j< grid[0].length; j++){
					if(grid[i][j]) maxCurrHeight=j+1;
				}

				if(maxCurrHeight > maxOne) maxOne=maxCurrHeight;
				if(maxCurrHeight != getColumnHeight(i))
					throw  new RuntimeException("There's a mistake in heights array");

			}

			if(getMaxHeight()!=maxOne)
				throw new RuntimeException("MaxHeight isn't correct");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	private boolean gridOutOfBounds(int x , int y){
		if(x> width-1 || x <0 || y> height-1 || y <0 ) return true;
		return false;
	}
	public int dropHeight(Piece piece, int x) {
		int maxx = 0;
		if(piece == null) throw new RuntimeException("Piece is empty");
		if(gridOutOfBounds(x,0) ||
				piece.getSkirt().length > width - x || piece.getHeight() > height )
				throw new RuntimeException("Piece is out of bounds");
		for(int i = 0; i < piece.getSkirt().length; i++) {
			int currDiff = heights[x+i]-piece.getSkirt()[i];
			if(currDiff>maxx) maxx=currDiff;
		}
		return maxx;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		if(gridOutOfBounds(x,0)) throw  new RuntimeException("column is out of bound");
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		if(gridOutOfBounds(0,y)) throw  new RuntimeException("row is out of bound");
		return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(gridOutOfBounds(x,y)) return  true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/

	private void oldInformation(){
		prevMaxHeight=maxHeight;
		for (int i=0; i< grid.length; i++) {
			System.arraycopy(grid[i], 0, prevGrid[i], 0, grid[i].length);
		}
		System.arraycopy(widths,0,prevWidths,0,widths.length);
		System.arraycopy(heights,0,prevHeights,0,heights.length);
	}

	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		if(piece == null) throw new RuntimeException("Piece is empty");
		committed = false;
		boolean filledRow = false;
		oldInformation();
		for(int i = 0; i < piece.getBody().length; i++) {
			int xx = x + piece.getBody()[i].x;
			int yy = y + piece.getBody()[i].y;
			if(gridOutOfBounds(xx,yy)) return PLACE_OUT_BOUNDS;
			if(grid[xx][yy]) return PLACE_BAD;
			grid[xx][yy] = true;
			widths[yy]++;
			if(widths[yy] == width) filledRow = true;
			if(yy+1 > heights[xx]) heights[xx] = yy + 1;
			if(yy + 1 > maxHeight) maxHeight = yy + 1;
		}

		sanityCheck();

		if(filledRow) return PLACE_ROW_FILLED;

		int result = PLACE_OK;
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed==true)oldInformation();
		committed = false;
		int rowsCleared = 0;
		for(int i = 0; i < grid[0].length; i++) {
			if(widths[i] == width) {
				rowsCleared +=1;
				for(int m=0; m< grid.length;m++){
					for(int k=i;k<grid[0].length-1;k++){
						grid[m][k]=grid[m][k+1];
					}
				}
				for(int n=0; n< grid.length; n++){
					grid[n][grid[0].length-1]=false;
				}
				for(int t=i; t< widths.length-1;t++){
					widths[t]=widths[t+1];
				}
				widths[widths.length-1]=0;
				i--;
			}
		}

		int maxx = 0;
		for(int i=0; i< grid.length;i++){
			int maxCurrHeight=0;
			for(int j=0; j< grid[0].length; j++){
				if(grid[i][j]) maxCurrHeight=j+1;
			}

			if(maxCurrHeight > maxx) maxx=maxCurrHeight;
			heights[i]=maxCurrHeight;

		}
		maxHeight=maxx;
		sanityCheck();
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed==false){
			maxHeight=prevMaxHeight;

			boolean[][] temp=grid;
			grid=prevGrid;
			prevGrid=temp;

			int[] temp1=widths;
			widths=prevWidths;
			prevWidths=temp1;

			int[] temp2=heights;
			heights=prevHeights;
			prevHeights=temp2;

			committed=true;
		}
		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


