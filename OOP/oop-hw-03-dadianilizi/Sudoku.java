import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	private int[][] firstVer;
	private int[][] grid;
	private int[][] firstAns;
	private int ans;
	private long time;
	private boolean alreadyCounted;

	private    class spot implements Comparable<spot> {
		private int row;
		private int  col;
		public spot(int row, int col) {
			this.row = row;
			this.col = col;
		}
		public void set(int num) {
			grid[row][col] = num;
		}
		public void delete() {
			grid[row][col] = 0;
		}

		public HashSet<Integer> possibleVariants(){
			HashSet<Integer> toReturn= new HashSet<>();
			fillHashset(toReturn);
			for(int i=0; i< grid[0].length; i++){
				if(i!=col){
					toReturn.remove(grid[row][i]);
				}
			}

			for(int j=0; j< grid.length; j++){
				if(j!=row){
					toReturn.remove(grid[j][col]);
				}
			}

			int leftStart=col-col%3;
			int topStart=row-row%3;
			for(int i=topStart; i< topStart+3; i++){
				for(int j=leftStart; j< leftStart+3 ; j++){
					if(i!=row && j!=col) toReturn.remove(grid[i][j]);

				}
			}

			return  toReturn;
		}

		@Override
		public int compareTo(spot o) {
			HashSet<Integer> current= this.possibleVariants();
			HashSet<Integer> toCompare= o.possibleVariants();
			return  current.size()-toCompare.size();
		}

	}

	private void fillHashset(HashSet<Integer> currHashset){
		currHashset.add(1);
		currHashset.add(2);
		currHashset.add(3);
		currHashset.add(4);
		currHashset.add(5);
		currHashset.add(6);
		currHashset.add(7);
		currHashset.add(8);
		currHashset.add(9);
	}

	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}

	public Sudoku(String text){
		this(textToGrid(text));
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid=ints;
		ans=0;

		firstVer=new int[SIZE][SIZE];
		for(int i=0; i< ints.length; i++){
			System.arraycopy(ints[i],0,firstVer[i],0,grid[0].length);
		}
		firstAns=new int[SIZE][SIZE];
		time=0;
		alreadyCounted=false;
	}
	
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {

		ans=0;
		Long startingTime=System.currentTimeMillis();
		ArrayList<spot> toSort=new ArrayList<>();
		for(int i=0; i< grid.length; i++){
			for(int j=0; j< grid[0].length; j++){
				if(grid[i][j]==0){
					spot newSpot=new spot(i,j);
					toSort.add(newSpot);
				}
			}
		}
		Collections.sort(toSort);
		int position=0;
		getAnswer(toSort,position);
		time=System.currentTimeMillis()-startingTime;
		return ans;
	}

	private void getAnswer(ArrayList<spot> toSort, int position) {
		if(ans >=100) return;
		if(position==toSort.size()){
			ans++;
			if(ans==1) {
				for(int i=0; i< grid.length; i++){
					System.arraycopy(grid[i],0,firstAns[i],0,grid[0].length);
				}
			}
			return;
		}
		HashSet<Integer> currSet= toSort.get(position).possibleVariants();
		spot currSpot=toSort.get(position);
		Iterator<Integer> i=currSet.iterator();
		while(i.hasNext()){
			currSpot.set(i.next());
			getAnswer(toSort, position+1);
			currSpot.delete();
		}


	}
	
	public String getSolutionText() {

		if(ans==0) return " ";
		StringBuilder ans=new StringBuilder();
		for(int i=0; i< firstVer.length; i++){
			for(int j=0; j< firstVer[0].length; j++){
				ans.append(firstAns[i][j]);
				if(j!=firstAns[0].length-1) ans.append(" ");
			}
			if(i!=firstAns.length-1) ans.append("\n");
		}

		StringBuilder currString=new StringBuilder();
		for(int i=0; i< grid.length; i++){
			for(int j=0; j< grid[0].length; j++){
				currString.append(grid[i][j]);
				if(j!=grid[0].length-1) currString.append(" ");
			}
			if(i!=grid.length-1) currString.append("\n");
		}

		return currString.toString();
	}
	
	public long getElapsed() {
		return time;
	}


}
