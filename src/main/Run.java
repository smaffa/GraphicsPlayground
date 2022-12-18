package main;

import programs.BezierCreator;
import programs.DerivativeExplorer;

import javax.swing.*;

public class Run {
	private static boolean isDev = false;
	
	public static void runBezierCreator() {
		SwingUtilities.invokeLater(new BezierCreator(isDev));
	}
	
	public static void runDerivativeExplorer() {
		SwingUtilities.invokeLater(new DerivativeExplorer());
	}
	
    public static void main(String[] args) {
//        runBezierCreator();
    	runDerivativeExplorer();
    }
}
