import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.sql.SQLSyntaxErrorException;
import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	private Piece StickShape, SShape,LShape, SquareShape;

	private Piece[] rotations;



	//constructor Test
	public void test1() {

		assertEquals(2,SShape.getHeight());
		assertEquals(3,SShape.getWidth());

		int[] SSkirt={0,0,1};
		assertTrue(Arrays.equals(SSkirt,SShape.getSkirt()));

		TPoint[] SBody={new TPoint(0,0), new TPoint(1,0), new TPoint(1,1), new TPoint(2,1)};
		assertTrue(Arrays.equals(SBody,SShape.getBody()));

		assertEquals(1, StickShape.getWidth());
		assertEquals(4, StickShape.getHeight());

		int[] StickSkirt = {0};
		assertTrue(Arrays.equals(StickSkirt, StickShape.getSkirt()));

		TPoint[] StickBody = {new TPoint(0,0), new TPoint(0,1), new TPoint(0,2), new TPoint(0,3)};
		assertTrue(Arrays.equals(StickBody,StickShape.getBody()));

	}

	//equals and computenext test
	public void test2() {

		//s
		Piece SShape1=SShape.computeNextRotation(); //first rotation
		Piece SShape2=SShape1.computeNextRotation(); //second rotation
		assertTrue(SShape.equals(SShape2));
		assertFalse(SShape.equals(SShape1));

		//L
		Piece LShape1=LShape.computeNextRotation();
		Piece LShape2=LShape1.computeNextRotation();
		Piece LShape3=LShape2.computeNextRotation();
		Piece LShape4=LShape3.computeNextRotation();

		assertTrue(LShape.equals(LShape4));
		assertFalse(LShape.equals(LShape1));
		assertFalse(LShape.equals(LShape2));
		assertFalse(LShape.equals(LShape3));

		//stick
		Piece Stick2 = StickShape.computeNextRotation();
		Piece Stick3 = Stick2.computeNextRotation();
		assertTrue(Stick3.equals(StickShape));
		assertFalse(Stick2.equals(StickShape));
		assertFalse(Stick2.equals(Stick3));

		//square
		Piece RotateSquare = SquareShape.computeNextRotation();
		Piece RotateSquare2 = RotateSquare.computeNextRotation();
		assertTrue(RotateSquare.equals(SquareShape));
		assertTrue(RotateSquare2.equals(SquareShape));
		assertTrue(RotateSquare2.equals(SquareShape));

	}

	public void test3(){

		//square
		Piece f1=rotations[Piece.SQUARE];
		assertTrue(f1.equals(f1.fastRotation()));
		assertTrue(f1.equals(f1.fastRotation().fastRotation()));
		assertTrue(f1.equals(f1.fastRotation().fastRotation().fastRotation()));


		//S2

		Piece f2=rotations[Piece.S2];

		assertFalse(f2.equals(f2.fastRotation()));
		assertTrue(f2.equals(f2.fastRotation().fastRotation()));

		//Stick
		Piece f3=rotations[Piece.STICK];

		assertFalse(f3.equals(f3.fastRotation()));
		assertTrue(f3.equals(f3.fastRotation().fastRotation().fastRotation().fastRotation()));
		assertTrue(f3.fastRotation().equals(f3.fastRotation().fastRotation().fastRotation()));


		//L2

		Piece f4=rotations[Piece.L2];

		assertTrue(f4.equals(f4.fastRotation().fastRotation().fastRotation().fastRotation()));
		assertFalse(f4.equals(f4.fastRotation().fastRotation().fastRotation()));

		//pyramid

		Piece f5=rotations[Piece.PYRAMID];
		assertFalse(f5.equals(f5.fastRotation()));
		assertFalse(f5.equals(f5.fastRotation().fastRotation()));
		assertFalse(f5.equals(
				f5.fastRotation().fastRotation().fastRotation()));
		assertTrue(f5.equals(f5.fastRotation().fastRotation().fastRotation().fastRotation()));

		assertTrue(f5.fastRotation().equals(pyr2));
		assertTrue(pyr3.equals(f5.fastRotation().fastRotation()));

	}




	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		StickShape = new Piece(Piece.STICK_STR);
		SShape = new Piece(Piece.S1_STR);
		SquareShape = new Piece(Piece.SQUARE_STR);
		LShape = new Piece(Piece.L2_STR);
		rotations = Piece.getPieces();

	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}


	public void testError() {
		try{
			Piece newOne=new Piece("x1234567");
			Assert.fail();
		} catch (Exception e){};
	}
	
}
