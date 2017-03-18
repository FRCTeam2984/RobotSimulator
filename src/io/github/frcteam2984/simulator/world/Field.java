package io.github.frcteam2984.simulator.world;

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
	
	/**
	 * The default constructor which creates a field 54'3" long by 26'3" wide
	 */
	public Field(){
		this(54*12 + 3, 26*12 + 3);
	}
	
	/**
	 * Creates a field with the given length and width in inches
	 * @param length the length in inches
	 * @param width the width in inches
	 */
	public Field(double length, double width){
		this.length = length;
		this.width = width;
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
	
}
