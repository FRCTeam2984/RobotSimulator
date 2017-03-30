package io.github.frcteam2984.codeloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import edu.wpi.first.wpilibj.IterativeRobot;

public class JarClassLoader {

	public static Class<? extends IterativeRobot> loadRobotMain(File jar, String name){
		System.out.println("Loading " + jar.getAbsolutePath());
		try {
			@SuppressWarnings("resource")
			URLClassLoader classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()});
			@SuppressWarnings("unchecked")
			Class<? extends IterativeRobot> robotMain = (Class<? extends IterativeRobot>) classLoader.loadClass(name);
			return robotMain;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e){
			System.err.println("Error: CAN NOT INSTANSIATE MAIN FROM IterativeRobot.java");
			throw new IllegalArgumentException();
		}
		return null;
	}
	
}
