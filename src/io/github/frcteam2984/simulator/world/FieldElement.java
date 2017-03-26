package io.github.frcteam2984.simulator.world;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A class which represents a field element
 * @author Max Apodaca
 *
 */
public class FieldElement {

	/**
	 * The position of this field element.
	 */
	private Point2D position;
	
	/**
	 * The polygon to be used for drawing
	 */
	private Path2D drawingPolygon;
	
	/**
	 * The complete body definition of the field element
	 */
	private CompleteBodyDefinition bodyDef;
	
	/**
	 * Creates a new Field element based on the JSON Object
	 * * <p><ul>
	 * <li>position: The position of the field element (JSON Object)
	 * <li>drawingPolygon: A JSONArray which contains the points for the drawing polygon
	 * </ul><p>
	 * @param json the json object to use to construct the field element.
	 */
	public FieldElement(JSONObject json){
		JSONObject position = json.getJSONObject("position");
		this.position = new Point2D.Double(position.getDouble("x"), position.getDouble("y"));
		
		//Drawing
		JSONArray drawingPoints = json.getJSONArray("drawing");
		this.drawingPolygon = new Path2D.Double();
		JSONObject startPoint = drawingPoints.getJSONObject(0);
		this.drawingPolygon.moveTo(startPoint.getDouble("x"), startPoint.getDouble("y"));
		for(int i = 1; i<drawingPoints.length(); i++){
			JSONObject point = drawingPoints.getJSONObject(i);
			this.drawingPolygon.lineTo(point.getDouble("x"), point.getDouble("y"));
		}
		this.drawingPolygon.closePath();
		AffineTransform transform = new AffineTransform();
		transform.translate(this.position.getX(), this.position.getY());
		this.drawingPolygon.transform(transform);
		
		//Collision
		JSONArray collisionPoints = json.getJSONArray("colliding");
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set((float)this.position.getX(), (float)this.position.getY());
		
		PolygonShape collisionBox = PolygonUtils.getPolygonFromJson(collisionPoints);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = collisionBox;
		this.bodyDef = new CompleteBodyDefinition(bodyDef, fixtureDef);
	}

	/**
	 * Returns the polygon to be drawn for this field element, it contains more detail than the collision polygon.
	 * @return the polygon to be drawn
	 */
	public Path2D getDrawingPath() {
		return this.drawingPolygon;
	}
	
	/**
	 * Returns the position of the fieldElement
	 * @return
	 */
	public Point2D getPostition(){
		return this.position;
	}
	
	/**
	 * Returns the body definition which can be used to create the field elements
	 * @return the body definition
	 */
	public CompleteBodyDefinition getBodyDefinition(){
		return this.bodyDef;
	}
	
}
