package io.github.frcteam2984.simulator.world;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * A class to represent all of the field elements and the field as a whole. The robot will be constrained to this.
 * The field contains field elements which may have special characteristics. For example a loading station has the ability to summon an entity
 * which is dispenses. However a wall will not have any special abilities.
 * @author Max Apodaca
 *
 */
public class Field {

	/**
	 * The length and width of the field
	 */
	private double length;
	private double width;
	
	private List<FieldElement> fieldElements;
	
	/**
	 * The default constructor which creates a field 54'3" long by 26'3" wide
	 */
	public Field(){
		this(54*12 + 3, 26*12 + 3);
	}
	
	/**
	 * Creates a field based on a json object. The following are the important keys. 
	 * <p><ul>
	 * <li>length: Length of the field (double)
	 * <li>width: Width of the field (double)
	 * <li>elements: Array of the field elements (JSON Object Array)
	 * </ul><p>
	 * @param json the json object to use to construct the field.
	 */
	public Field(JSONObject json){
		this(json.getDouble("length"), json.getDouble("width"));
		for(Object objectFromJson : json.getJSONArray("elements")){
			JSONObject fieldElementJSON = (JSONObject) objectFromJson;
			FieldElement fieldElement = new FieldElement(fieldElementJSON);
			this.fieldElements.add(fieldElement);
		}
	}
	
	/**
	 * Creates a field with the given length and width in inches
	 * @param length the length in inches
	 * @param width the width in inches
	 */
	public Field(double length, double width){
		this.length = length;
		this.width = width;
		this.fieldElements = new ArrayList<FieldElement>();
	}
	
	/**
	 * Returns the height of the field
	 * @return the height in inches
	 */
	public double getLength(){
		return this.length;
	}
	
	/**
	 * Returns the width of the field
	 * @return the width in inches
	 */
	public double getWidth(){
		return this.width;
	}
	
	/**
	 * Returns a list of shapes which make up the drawing elements of the field including the border of the field.
	 * @return the shapes to be drawn
	 */
	public List<Path2D> getDrawingPaths(){
		ArrayList<Path2D> paths = new ArrayList<Path2D>(this.fieldElements.size() + 1);
		for(FieldElement element : this.fieldElements){
			paths.add(element.getDrawingPath());
		}
		paths.add(new Path2D.Double(new Rectangle.Double(-this.length/2, -this.width/2, this.length, this.width)));
		return paths;
	}
	
	/**
	 * Returns a list of shapes which make up the collision elements, this excludes the other perimeter of the field
	 * @return the collision shapes of the field
	 */
	public List<Shape> getCollisionShapes(){
		return null;
	}
	
}