package main;

import javax.swing.*;

import main.programs.BezierCreator;
import main.programs.DerivativeExplorer;

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
