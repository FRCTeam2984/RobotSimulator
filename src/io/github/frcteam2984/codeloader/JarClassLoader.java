package io.github.frcteam2984.codeloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import edu.wpi.first.wpilibj.IterativeRobot;
import io.github.frcteam2984.simulator.VisionAndRobotClass;
import io.github.frcteam2984.simulator.vision.RobotWorldAcceptor;

public class JarClassLoader {

	private static List<Class> classes = new ArrayList<Class>();
	
	public static Class<? extends IterativeRobot> loadRobotMain(File jar, String name, String[] dontLoad){
		System.out.println("Loading " + jar.getAbsolutePath());
		try {
			JarFile jarFile = new JarFile(jar);
			URLClassLoader classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()});
			@SuppressWarnings("unchecked")
			Class<? extends IterativeRobot> robotMain = (Class<? extends IterativeRobot>) classLoader.loadClass(name);
			Enumeration<JarEntry> e = jarFile.entries();
			ElementLoop:
			while(e.hasMoreElements()){
				JarEntry je = e.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class")){
					continue;
				}
				String className = je.getName().substring(0,je.getName().length()-6);
			    className = className.replace('/', '.');
				for(String notToLoad : dontLoad){
					if(className.equals(notToLoad)){
						continue ElementLoop;
					}
				}
					System.out.println(className);
				classes.add(classLoader.loadClass(className));
			}
			jarFile.close();
			classLoader.close();
			return robotMain;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e){
			System.err.println("Error: CAN NOT INSTANSIATE MAIN FROM IterativeRobot.java");
			throw new IllegalArgumentException();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends RobotWorldAcceptor>[] loadVisionAugments(File jar, String[] names){
		System.out.println("Loading " + jar.getAbsolutePath());
		try {
			@SuppressWarnings("resource")
			URLClassLoader classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()});
			Class<? extends RobotWorldAcceptor>[] classes = new Class[names.length];
			for(int i = 0; i<names.length; i++){
				System.out.println(names[i]);
				classes[i] = (Class<? extends RobotWorldAcceptor>) classLoader.loadClass(names[i]);
			}
			return classes;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e){
			System.err.println("Error: Unable to load vision augments");
			throw new IllegalArgumentException();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static VisionAndRobotClass testComboLoad(File robotCode, File visionAugment, String name, String[] visionAugmentClasses){
		try {
			@SuppressWarnings("resource")
			URLClassLoader classLoader = new URLClassLoader(new URL[]{visionAugment.toURI().toURL(), robotCode.toURI().toURL()});
			Class<? extends IterativeRobot> robotMain = (Class<? extends IterativeRobot>) classLoader.loadClass(name);
			Class<? extends RobotWorldAcceptor>[] classes = new Class[visionAugmentClasses.length];
			for(int i = 0; i<visionAugmentClasses.length; i++){
				System.out.println(visionAugmentClasses[i]);
				classes[i] = (Class<? extends RobotWorldAcceptor>) classLoader.loadClass(visionAugmentClasses[i]);
			}
			return new VisionAndRobotClass(robotMain, classes);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e){
			System.err.println("Error: CAN NOT INSTANSIATE MAIN FROM IterativeRobot.java");
			throw new IllegalArgumentException();
		}
		return null;
	}
	
}
