package io.github.frcteam2984.simulator.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import io.github.frcteam2984.simulator.world.Robot;
import io.github.frcteam2984.simulator.world.SimulationWorld;

/**
 * A panel to do the rendering of the field and its elements
 * @author Max Apodaca
 *
 */
public class SimulationPane extends JPanel implements Observer, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9205063175488138762L;

	/**
	 * a local instance of the world
	 */
	private SimulationWorld world;

	/**
	 * the scale, x, and y
	 */
	private double scale, x, y;

	/**
	 * the constructor of the pane that gets a singleton of the {@link io.github.frcteam2984.simulator.ui.RenderManager RenderManager}
	 * class using the {@link io.github.frcteam2984.simulator.ui.RenderManager#getInstance() getInstance} method
	 */
	public SimulationPane(){
		this.setBackground(new Color(0xFFFFFF));
		this.scale = 2;
		this.x = 0;
		this.y = 0;
	}

	/**
	 * constructor to use when you don't want a 1inch to 2pixle scale
	 * @param scale the scale to start out with
	 */
	public SimulationPane(double scale){
		this();
		this.scale = scale;
	}

	/**
	 * calls the appropriate {@link io.github.frcteam2984.simulator.ui.RenderHandler RenderHandlers} to draw each element in the world
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(world == null)
			return;
		double width = this.getWidth();
		double height = this.getHeight();
		((Graphics2D)g).translate(width/2, height/2);
		((Graphics2D)g).scale(this.scale, this.scale);

		for(Shape shape : world.getField().getDrawingPaths()){
			((Graphics2D)g).draw(shape);
		}
		Graphics2D robotGraphics = (Graphics2D)g.create();
		Robot robot = world.getRobot();
		robotGraphics.translate(robot.getPos().x, robot.getPos().y);
		robotGraphics.rotate(robot.getAngle());
		robotGraphics.draw(robot.getPath());
		robotGraphics.dispose();
	}

	/**
	 * sets the scale of the world
	 * @param scale the new scale
	 */
	public void setScale(double scale){
		this.scale = scale;
	}

	/**
	 * sets the x and y of the view
	 * @param x the x
	 * @param y the y
	 */
	public void setXandY(double x, double y){
		this.x = x;
		this.y = y;
	}

//	static int i = 0;
	
	/**
	 * Called when the world is updated and repaints the screen
	 */
	@Override
	public void update(Observable world, Object message) {
		this.world = (SimulationWorld) world;
//		if(this.getWidth() > 0 && this.getHeight() > 0){
//			BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//			this.paint(image.getGraphics());
//			new Thread(){
//				@Override
//				public void run(){
//					NumberFormat format = NumberFormat.getIntegerInstance();
//					format.setGroupingUsed(false);
//					format.setMaximumIntegerDigits(5);
//					format.setMinimumIntegerDigits(5);
//					File output = new File("/tmp/FRCSim/frame_" + format.format(i++) + ".png");
//					try {
//						ImageIO.write(image, "png", output);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}.start();
//			
//		}
		repaint();
	}

	private boolean[] pressed = new boolean[128];
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		pressed[arg0.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		pressed[arg0.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}
