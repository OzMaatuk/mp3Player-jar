package Displayables;

import Data_Structures.AlbumList;
import Data_Structures.ArtistList;
import Data_Structures.SongList;
import Memory_Management.LibraryUpdater;
import Memory_Management.Lists;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Update songs library
 * @author
 */
public class UpdateLibrary extends Canvas {
	private MainMenu menu;
	private Thread trdUpdater;
	private LibraryUpdater libUpdtr;
	private Image bgImage;
	private boolean isFinished;
	private boolean isFirstTime;
	private int width;
	private int height;
	static final Font txtFont = Font.getFont (Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	static final Font finishFont = Font.getFont (Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
	/**
	 * Default c'tor
	 * @param menu MainMenu containing this updater
	 */
	public UpdateLibrary(MainMenu menu)
	{
		this.menu = menu;
		this.isFinished = false;
		this.width = this.getWidth();
		this.height = this.getHeight();
		try {
			this.bgImage = Image.createImage("/Images/BG.png");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//Check if this is the first read
		if (menu.songs == null)
		{
			//First time updating library
			this.isFirstTime = true;
		}
		else
		{
			//Reupdate the library again
			this.isFirstTime = false;
		}

		//Run the thread
		this.libUpdtr = new LibraryUpdater(this);
		this.trdUpdater = new Thread(libUpdtr);
		this.trdUpdater.start();
	}

	/**
	 * Finish indicates that reading songs from the device's memory is done
	 * @param songs Songs read from the device
	 */
	public void Finish(SongList songs)
	{
		songs.Sort();
		this.menu.songs = songs;
		this.menu.artists = new ArtistList();
		this.menu.albums = new AlbumList();
		this.isFinished = true;
		this.repaint();
		Lists.CreateAllLists(songs, this.menu.albums, this.menu.artists);
		try {
			Thread.sleep(50);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		this.menu.getMainMIDlet().getCurrentDisplay().setCurrent(this.menu);
	}

	/**
	 * paint function that paints a string saying 'Updating" or "Finished"
	 * @param g Graphics component
	 */
	protected void paint(Graphics g) {
		g.drawImage(bgImage, 0, 0, 0);
		g.setColor(0x00000000); // Black
		if (this.isFinished)
		{
			g.setFont(finishFont);
			String txt = "Finished";
			g.drawString(txt,
				width/2 - finishFont.stringWidth(txt) / 2,
				height/2 - 20,
				20);
		}
		else
		{
			g.setFont(txtFont);
			if (this.isFirstTime)
			{
				String txt = "Updating library";
				g.drawString(txt,
					width/2 - txtFont.stringWidth(txt) / 2,
					height/2 - 15,
					20);
				txt = "for the first time";
				g.drawString(txt,
					width/2 - txtFont.stringWidth(txt) / 2,
					height/2 + 15,
					20);
			}
			else
			{
				String txt = "Updating library";
				g.drawString(txt,
					width/2 - txtFont.stringWidth(txt) / 2,
					height/2 - 20,
					20);
			}
		}
	}
}
