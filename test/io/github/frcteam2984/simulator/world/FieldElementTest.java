package io.github.frcteam2984.simulator.world;

import static org.junit.Assert.*;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;

import org.json.JSONObject;
import org.junit.Test;

/**
 * A test to test the FieldElement class
 * @author Max Apodaca
 *
 */
public class FieldElementTest {

	
	@Test
	public void triangularFieldElementCreatesTriangleDrawingShape(){
		JSONObject json = new JSONObject("{"
				+ "position:{x:0, y:0},"
				+ "drawingType:\"Polygon\","
				+ "drawing:[{x:0, y:0},{x:1, y:0},{x:0, y:1}]"
			+ "}");
		FieldElement element = new FieldElement(json);
		Path2D actual = element.getDrawingPath();
		Path2D expected = new Path2D.Double();
		expected.moveTo(0, 0);
		expected.lineTo(1, 0);
		expected.lineTo(0, 1);
		expected.closePath();
		assertTrue(equal(actual, expected));
	}
	
	@Test
	public void fieldElementsPositionCrrispondsToJSONPosition(){
		JSONObject json = new JSONObject("{"
				+ "position:{x:29, y:84},"
				+ "drawingType:\"Polygon\","
				+ "drawing:[{x:0, y:0},{x:1, y:0},{x:0, y:1}]"
			+ "}");
		FieldElement element = new FieldElement(json);
		assertEquals(29, element.getPostition().getX(), 0.00001);
		assertEquals(84, element.getPostition().getY(), 0.00001);
	}
	
	/**
	   * Tests two paths for equality.  If both are <code>null</code> this
	   * method returns <code>true</code>.
	   *
	   * @param p1  path 1 (<code>null</code> permitted).
	   * @param p2  path 2 (<code>null</code> permitted).
	   *
	   * @return A boolean.
	   */
	  private static boolean equal(Path2D p1, Path2D p2) {
	      if (p1 == null) {
	          return (p2 == null);
	      }
	      if (p2 == null) {
	          return false;
	      }
	      PathIterator iterator1 = p1.getPathIterator(null);
	      PathIterator iterator2 = p2.getPathIterator(null);
	      while(!iterator1.isDone()){
	    	  if(iterator2.isDone()){
	    		  return false;
	    	  }
	    	  double[] coords1 = new double[2];
	    	  double[] coords2 = new double[2];
	    	  if(iterator1.currentSegment(coords1) != iterator2.currentSegment(coords2)){
	    		  return false;
	    	  }
	    	  if(!Arrays.equals(coords1, coords2)){
	    		  return false;
	    	  }
	    	  iterator1.next();
	    	  iterator2.next();
	      }
	      return iterator2.isDone();
	  }
}
