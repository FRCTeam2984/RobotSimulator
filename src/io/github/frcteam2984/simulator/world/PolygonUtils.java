package io.github.frcteam2984.simulator.world;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.json.JSONArray;
import org.json.JSONObject;

public class PolygonUtils {

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
	
}
