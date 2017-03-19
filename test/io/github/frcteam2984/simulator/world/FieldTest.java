package io.github.frcteam2984.simulator.world;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Path2D;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * A test to test the Field class
 * @author Max Apodaca
 *
 */
public class FieldTest {

	private Field field;
	
	@Before
	public void before(){
		this.field = new Field();
	}
	
	@Test
	public void emptyFieldReturnsOneRectangleAsDrawingShape(){
		List<Path2D> drawingPaths = this.field.getDrawingPaths();
		assertEquals(1, drawingPaths.size());
	}
	
	@Test
	public void heightAndWidthSetFromJsonInConstructor(){
		this.field = new Field(new JSONObject("{"
					+ "width: 10,"
					+ "length: 20,"
					+ "elements:[]"
				+ "}"));
		assertEquals(20, this.field.getLength(), 0.00001);
		assertEquals(10, this.field.getWidth(), 0.00001);
	}
	
	@Test
	public void createsOneElementWhenZeroElementsInArray(){
		JSONObject json = new JSONObject("{"
				+ "width: 10,"
				+ "length: 20,"
				+ "elements:[]"
			+ "}");
		this.field = new Field(json);
		assertEquals(1, this.field.getDrawingPaths().size());
	}
	
	@Test
	public void createsTwoElementsWhenOneElementInArray(){
		JSONObject json = new JSONObject("{"
				+ "width: 10,"
				+ "length: 20,"
				+ "elements:[]"
			+ "}");
		JSONObject fieldElement = new JSONObject("{"
				+ "position:{x:0, y:0},"
				+ "drawingType:\"Polygon\","
				+ "drawing:[{x:0, y:0},{x:1, y:0},{x:0, y:1}]"
			+ "}");
		json.getJSONArray("elements").put(fieldElement);
		this.field = new Field(json);
		assertEquals(2, this.field.getDrawingPaths().size());
	}
	
	@Test(expected=JSONException.class)
	public void throwsInvalidParameterExceptionGivenNoHeightOrWidth(){
		this.field = new Field(new JSONObject("{}"));
	}
	
}
