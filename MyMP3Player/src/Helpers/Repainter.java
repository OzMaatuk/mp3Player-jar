package Helpers;

import Displayables.Mp3Player;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;

/**
 * Repainter calls repaint on a given canvas
 * @author
 */
public class Repainter extends TimerTask {
	Canvas canvas;
	/**
	 * Default c'tor
	 * @param cnvsToRepnt Mp3Player instance containing this repainter
	 */
	public Repainter(Mp3Player cnvsToRepnt)
	{
		super();
		this.canvas = cnvsToRepnt;
	}
	/**
	 * run function for thread starts - calls repaint on the given canvas
	 */
	public void run() {
		this.canvas.repaint();
	}

}
