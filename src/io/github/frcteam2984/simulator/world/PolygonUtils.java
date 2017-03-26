package io.github.frcteam2984.simulator.world;

import java.awt.geom.Path2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A class of useful methods for working with polygons and paths
 * @author Maximilian Apodaca
 *
 */
public class PolygonUtils {

	/**
	 * Creates a polygon shape from an array of JSON objects containing x and y coordinates
	 * @param jsonVerticies the array of vertices
	 * @return a polygon shape representative of the vertices.
	 */
	public static PolygonShape getPolygonFromJson(JSONArray jsonVerticies){
		PolygonShape polygon = new PolygonShape();
		Vec2[] verts = new Vec2[jsonVerticies.length()];
		for(int i = 0; i<jsonVerticies.length(); i++){
			JSONObject vert = jsonVerticies.getJSONObject(i);
			float x = (float)vert.getDouble("x");
			float y = (float)vert.getDouble("y");
			verts[i] = new Vec2(x, y);
		}
		polygon.set(verts, jsonVerticies.length());
		return polygon;
	}
	
	/**
	 * Creates a Path2D representation of the vertices passed in as a JSON array containing x and y coordinates.
	 * @param jsonVerticies the vertices to use
	 * @return a Path2D representation of the vertices.
	 */
	public static Path2D getPathFromJson(JSONArray jsonVerticies){
		Path2D path = new Path2D.Double();
		JSONObject startPoint = jsonVerticies.getJSONObject(0);
		path.moveTo(startPoint.getDouble("x"), startPoint.getDouble("y"));
		for(int i = 1; i<jsonVerticies.length(); i++){
			JSONObject point = jsonVerticies.getJSONObject(i);
			path.lineTo(point.getDouble("x"), point.getDouble("y"));
		}
		path.closePath();
		return path;
	}
	
}
