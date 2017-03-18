package io.github.frcteam2984.simulator.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawingUtils {

	/**
	 * Draws a centered non filled rectangle on the graphics with the given width and height
	 * @param g the graphics to draw with
	 * @param x the x position
	 * @param y the y position
	 * @param width the width
	 * @param height the height
	 * @param color the color which the lines should be
	 */
	public static void drawCenteredRectangle(Graphics g, double x, double y, double width, double height, Color color){
		Graphics2D g2 = (Graphics2D)g.create();
		g2.translate(x, y);
		g2.setColor(color);
		g2.scale(width/2, height/2);
		g2.drawRect(-1, -1, 2, 2);
		g2.dispose();
	}
	
}
