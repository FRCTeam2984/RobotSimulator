package io.github.frcteam2984.simulator.world;

import java.util.List;

public class SATSolver {
	
	/**
	 * takes shape arguments and uses the correct method to calculate intersection
	 * if they are of type {@link physics.Polygon Polygon} or {@link physics.Circle Circle}.
	 * @param s1 the first shape
	 * @param s2 the second shape
	 * @return whether or not the two shapes are intersecting
	 */
	public static boolean intersect(Shape s1, Shape s2){
		if(s1.getClass().equals(Polygon.class) && s2.getClass().equals(Polygon.class)){
			return SATSolver.intersect((Polygon) s1, (Polygon) s2);
		}
		if(s1.getClass().equals(Polygon.class) && s2.getClass().equals(Circle.class)){
			return SATSolver.intersect((Polygon) s1, (Circle) s2);
		}
		if(s1.getClass().equals(Circle.class) && s2.getClass().equals(Circle.class)){
			return SATSolver.intersect((Circle) s1, (Circle) s2);
		}
		if(s1.getClass().equals(Circle.class) && s2.getClass().equals(Polygon.class)){
			return SATSolver.intersect((Polygon) s2, (Circle) s1);
		}
		return false;
	}
	
	/**
	 * tells whether two circle are intersecting
	 * @param c1 the first circle
	 * @param c2 the second circle
	 * @return whether they are inspecting
	 */
	public static boolean intersect(Circle c1, Circle c2){
		double distance = new Vector(c1.getCenter(), c2.getCenter()).getLength();
		return distance <= c1.getRadius() + c2.getRadius();
	}
	
	/**
	 * SAT collision check between two convex polygons
	 * @param p1 the first convex polygon
	 * @param p2 the second convex polygon
	 * @return whether or not the two polygons are intersecting each other
	 */
	public static boolean intersect(Polygon p1, Polygon p2){
		if(p1.getPoints().isEmpty())
			return false;
		if(p2.getPoints().isEmpty())
			return false;
		BoundingBox boxOne = p1.getBoundingBox();
		BoundingBox boxTwo = p2.getBoundingBox();
		if(!boxOne.intersect(boxTwo)){
			return false;
		}
		int size = p1.getPoints().size();
		List<Point> points = p1.getPoints();
		for(int i = 0; i<size; i++){
			Vector normal = new Vector(points.get(i), points.get((i+1)%size)).normal();
			double[] maxMinP1 = SATSolver.maxAndMinSAT(p1, normal);
			double[] maxMinP2 = SATSolver.maxAndMinSAT(p2, normal);
			if(maxMinP1[1] > maxMinP2[0] || maxMinP1[0] < maxMinP2[1]){
				return false;
			}
		}
		size = p2.getPoints().size();
		points = p2.getPoints();
		for(int i = 0; i<size; i++){
			Vector normal = new Vector(points.get(i), points.get((i+1)%size)).normal();
			double[] maxMinP1 = SATSolver.maxAndMinSAT(p1, normal);
			double[] maxMinP2 = SATSolver.maxAndMinSAT(p2, normal);
			if(maxMinP1[1] > maxMinP2[0] || maxMinP1[0] < maxMinP2[1]){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * uses SAT with the closest point to the circle of the polygon to figure out if they are intersecting
	 * @param p1 the polygon
	 * @param c1 the circle
	 * @return whether they are intersecting or not
	 */
	public static boolean intersect(Polygon p1, Circle c1){
		if(p1.getPoints().isEmpty())
			return false;
		Point closest = p1.getPoints().get(0).clone().translate(p1.getCenter());
		double dist = new Vector(closest, c1.getCenter()).getLength();
		List<Point> points = p1.getPoints();
		for(int i = 1; i<points.size(); i++){
			Point p = points.get(i).clone().translate(p1.getCenter());
			double newDist = new Vector(p, c1.getCenter()).getLength();
			if(newDist < dist){
				closest = p;
				dist = newDist;
			}
		}
		Vector axis = new Vector(closest, c1.getCenter());
		double[] maxMinP1 = SATSolver.maxAndMinSAT(p1, axis);
		double circleCenterDistance = c1.getCenter().projectOntoAxis(axis);
		double deltaCircle = c1.getRadius() / axis.getLength();
		if(maxMinP1[0] < circleCenterDistance-deltaCircle || maxMinP1[1] > circleCenterDistance+deltaCircle){
			return false;
		}
		axis = new Vector(p1.getCenter(), c1.getCenter());
		maxMinP1 = SATSolver.maxAndMinSAT(p1, axis);
		circleCenterDistance = c1.getCenter().projectOntoAxis(axis);
		deltaCircle = c1.getRadius() / axis.getLength();
		if(maxMinP1[0] < circleCenterDistance-deltaCircle || maxMinP1[1] > circleCenterDistance+deltaCircle){
			return false;
		}
		return true;
	}
	
	/**
	 * finds the max and min points of the polygon projected onto the axis. this is O(n)
	 * @param polygon the polygon to project
	 * @param axis the axis to project on to
	 * @return the [max,min] maximum and minium values
	 */
	private static double[] maxAndMinSAT(Polygon polygon, Vector axis){
		if(polygon.getPoints().size() < 1)
			return new double[]{0D,0D};
		double dist = polygon.getPoints().get(0).projectOntoAxis(axis);
		double[] maxAndMin = new double[]{dist,dist};
		for(Point p : polygon.getPoints()){
			dist = p.projectOntoAxis(axis);
			maxAndMin[0] = Math.max(maxAndMin[0], dist);
			maxAndMin[1] = Math.min(maxAndMin[1], dist);
		}
		double centerDist = polygon.getCenter().projectOntoAxis(axis);
		maxAndMin[0] += centerDist;
		maxAndMin[1] += centerDist;
		return maxAndMin;
	}
	
	/**
	 * Point shape intersection
	 * @param p the point to test
	 * @param shape the shape
	 * @return whether or not they are intersecting
	 */
	public static boolean intersect(Point p, Shape shape){
		if(shape == null){
			return false;
		}
		if(shape.getClass().equals(Circle.class)){
			Circle circle = (Circle)shape;
			double distance = new Vector(p, circle.getCenter()).getLength();
			return distance <= circle.getRadius();
		} else if(shape.getClass().equals(Polygon.class)){
			Polygon p1 = (Polygon) shape;
			int size = p1.getPoints().size();
			List<Point> points = p1.getPoints();
			for(int i = 0; i<size; i++){
				Vector normal = new Vector(points.get(i), points.get((i+1)%size)).normal();
				double[] maxMinP1 = SATSolver.maxAndMinSAT(p1, normal);
				double pDist = p.projectOntoAxis(normal);
				if(maxMinP1[1] > pDist || maxMinP1[0] < pDist){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
