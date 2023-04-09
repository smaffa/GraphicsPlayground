package main;

import javax.swing.*;

import main.programs.BezierCreator;
import main.programs.ContinuityExplorer;
import main.programs.CurveRider;
import main.programs.DerivativeExplorer;

/**
 * Class for launching individual programs.
 * @author smaffa
 *
 */
public class Run {
	private static boolean isDev = false; // flag for running development versions of each program
	
	public static void runBezierCreator() {
		SwingUtilities.invokeLater(new BezierCreator(isDev));
	}
	
	public static void runDerivativeExplorer() {
		SwingUtilities.invokeLater(new DerivativeExplorer());
	}
	
	public static void runCurveRider() {
		SwingUtilities.invokeLater(new CurveRider());
	}
	
	public static void runContinuityExplorer() {
		SwingUtilities.invokeLater(new ContinuityExplorer());
	}
	
    public static void main(String[] args) {
//    	runBezierCreator();
//    	runDerivativeExplorer();
//    	runCurveRider();
    	runContinuityExplorer();
    }
}
