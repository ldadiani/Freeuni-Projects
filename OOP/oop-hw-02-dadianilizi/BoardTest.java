import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	Piece L, L1,L2,L3;
	Piece square;
	Piece stick;
	Piece mirrorS;


	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		b.place(pyr1, 0, 0);

		Piece[] arr=Piece.getPieces();

		L=arr[Piece.L1];
		L1=L.fastRotation();
		L2=L1.fastRotation();
		L3=L2.fastRotation();

		square=arr[Piece.SQUARE];

		stick=arr[Piece.STICK];

		mirrorS=arr[Piece.S2];
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Makre  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.


	public  void testConst(){
		b=new Board(3,5);
		assertEquals(b.getHeight(),5);
		assertEquals(b.getWidth(),3);
		assertEquals(b.getColumnHeight(0),0);
		assertEquals(b.getColumnHeight(1),0);
		assertEquals(b.getMaxHeight(),0);
		assertFalse(b.getGrid(1, 4));
		assertEquals(b.getRowWidth(3),0);
	}

	public void testPlace(){

		b=new Board(3,4);
		assertEquals(Board.PLACE_ROW_FILLED,b.place(pyr1,0,0));;
		assertEquals(b.getRowWidth(0),3);
		assertEquals(b.getRowWidth(1),1);
		assertEquals(b.getRowWidth(2),0);
		assertEquals(b.getRowWidth(3),0);
		assertEquals(b.getColumnHeight(0),1);
		assertEquals(b.getColumnHeight(1),2);
		assertEquals(b.getColumnHeight(2),1);
		assertEquals(b.getMaxHeight(),2);
		assertEquals(b.dropHeight(L3,0),1);
		b.commit();

		assertEquals(Board.PLACE_OK, b.place(square,0,2));
		assertEquals(3,b.getRowWidth(0));
		assertEquals(1,b.getRowWidth(1));
		assertEquals(2,b.getRowWidth(2));
		assertEquals(2,b.getRowWidth(3));
		assertEquals(4,b.getColumnHeight(0));
		assertEquals(4,b.getColumnHeight(1));
		assertEquals(1,b.getColumnHeight(2));
		assertEquals(4,b.getMaxHeight());
		assertEquals(4,b.dropHeight(square,0));
		assertEquals(2,b.dropHeight(L2,1));
	}

	public void testUndo(){
		b=new Board(4,4);
		String empty=b.toString();
		assertEquals(Board.PLACE_OK,b.place(square,0,0));
		b.undo();
		assertTrue(empty.equals(b.toString()));

		assertEquals(Board.PLACE_OK,b.place(pyr1,1,0));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED,b.place(stick,0,0));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED,b.place(L3,1,1));
		String threeFigure=b.toString();
		b.commit();
		assertEquals(b.clearRows(),2);

		b.undo();
		assertTrue(threeFigure.equals(b.toString()));
		assertEquals(Board.PLACE_BAD,b.place(L,2,2));
		b.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS,b.place(stick,3,3));
	}

	public void testAll(){

		b=new Board(5,5);
		assertEquals(b.place(s,0,0),Board.PLACE_OK);
		b.commit();
		String firstVar=b.toString();
		assertEquals(b.place(stick,1,0), Board.PLACE_BAD);
		b.undo();
		String secondVar=b.toString();
		assertTrue(firstVar.equals(secondVar));


		assertEquals(b.place(stick,1,0), Board.PLACE_BAD);
		b.undo();

		assertEquals(b.place(stick,0,1),Board.PLACE_OK);
		b.commit();


		assertEquals(b.place(square,3,2),Board.PLACE_OK);
		b.commit();

		assertEquals(Board.PLACE_ROW_FILLED,b.place(square,3,0));
		assertEquals(1,b.clearRows());

	}


	public void  testError1(){
		try{
			b=new Board(5,5);
			b.dropHeight(square,7);
		} catch (Exception e){};

	}

	public void  testError2(){
		try{
			b=new Board(2,2);
			b.dropHeight(square,1);
		} catch (Exception e){};

	}


	public void  testError3(){
		try{
			b=new Board(5,5);
			b.dropHeight(null,1);
		} catch (Exception e){};

	}

	public void  testError4(){
		try{
			b=new Board(2,2);
			b.dropHeight(stick,1);
		} catch (Exception e){};

	}



	public void testError5(){
		try {
			b=new Board(3,3){
				@Override
				public int getRowWidth(int x){
					return  -1;
				}

			};
			b.place(s,0,0);

		}catch (Exception ignored){};

	}

	public void testError6(){
		try {
			b=new Board(3,3){
				@Override
				public int getColumnHeight(int x){
					return  -1;
				}
			};
			b.place(s,0,0);

		}catch (Exception ignored){};

	}

	public void testError7(){
		try {
			b=new Board(3,3){
				@Override
				public int getMaxHeight(){
					return  -1;
				}

			};
			b.place(s,0,0);

		}catch (Exception ignored){};

	}

}
