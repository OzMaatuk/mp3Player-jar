package Displayables;

import Data_Structures.Song;
import Data_Structures.SongList;
import Helpers.Repainter;
import Memory_Management.Settings;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * Mp3 Player - Main Canvas that plays the songs
 * @author
 */
public class Mp3Player extends Canvas implements CommandListener, PlayerListener {

    private Player player;
    private VolumeControl vc;
    private InputStream is;
    private SongList songs;
    private int currSongIndex;
    private long songTotalTime;
    private long songCurrentTime;
    private Image bgImage;
    private Image imgPlay;
    private Image imgPrev;
    private Image imgNext;
    private Image imgPrevPressed;
    private Image imgNextPressed;
    private Image imgPause;
    private Command cmndBack;
    private List container;
    private MainMenu menu;
    private Font songNameFont = Font.getFont (Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
    private Font songTimeFont = Font.getFont (Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private boolean isForwardBlue;
    private boolean isRewindBlue;
    private Timer timer;

    /**
     * Default c'tor
     * @param menu Main menu
     * @param container List containing this canvas
     * @param songs Songs to play
     * @param selectedSongIndex Index of song to start from
     */
    public Mp3Player(MainMenu menu, List container, SongList songs, int selectedSongIndex)
	{
		super();
		//Initialize variables
		this.songs = songs.clone();
		//Check if shuffle is needed
		if (menu.settings.getShuffle()) {
			/*Save song to play and then play it after the shuffle*/
			Song song2Play = this.songs.ValueAt(selectedSongIndex);
			this.songs.Shuffle();
			selectedSongIndex = this.songs.IndexOf(song2Play);
		}
		//Add back command
		this.cmndBack = new Command("Back", Command.BACK, 2);
		this.addCommand(cmndBack);
		//Initialize vars
		this.container = container;
		this.menu = menu;
		this.isForwardBlue = this.isRewindBlue = false;
		//Set listener
		this.setCommandListener(this);
		//Load images
		try {
			this.bgImage = Image.createImage("/Images/BG.png");
			this.imgPlay = Image.createImage("/Images/Play.png");
			this.imgPrev = Image.createImage("/Images/Prev.png");
			this.imgNext = Image.createImage("/Images/Next.png");
			this.imgPrevPressed = Image.createImage("/Images/PrevPressed.png");
			this.imgNextPressed = Image.createImage("/Images/NextPressed.png");
			this.imgPause = Image.createImage("/Images/Pause.png");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//Start playing
		this.changeSong(selectedSongIndex);
		//Set timer
		timer = new Timer();
		timer.scheduleAtFixedRate(new Repainter(this), 500, 500);
	}

	/**
	 * Change song
	 * @param newSongIndex Index of the new song
	 */
	public void changeSong(int newSongIndex)
	{
		this.currSongIndex = newSongIndex;
		try {
			if (this.player != null){
				this.player.stop();
				this.player.deallocate();
				this.player.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			if (this.menu.currentPlayer != null)
			{
				if (this.menu.currentPlayer.getState() != this.menu.currentPlayer.CLOSED)
				{
					this.menu.currentPlayer.stop();
					this.menu.currentPlayer.deallocate();
					this.menu.currentPlayer.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.startPlaying();
	}

	/**
	 * Start playing the current song
	 */
	public void startPlaying()
	{
		  try
		  {
			try {
				FileConnection fc = (FileConnection) Connector.open(this.songs.ValueAt(currSongIndex).getPath());
				if (fc.canRead())
				{
					is = fc.openInputStream();
					try {
						player = Manager.createPlayer(is, "audio/mpeg");
					} catch (Exception ex) {
						ex.printStackTrace();
						return;
					}

					this.menu.currentPlayer = player;

					player.realize();

					//get volume control
					vc = (VolumeControl) player.getControl("VolumeControl");

					if(vc != null)
						vc.setLevel(this.menu.settings.getVolume());

					player.prefetch();
					songTotalTime = player.getDuration();
					songCurrentTime = 0;
					
					player.start();

					//Set listener
					player.addPlayerListener(this);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		catch(IllegalArgumentException iae)
		{
			System.out.println("InputStream is NULL");
			iae.printStackTrace();
		}
		catch(MediaException me)
		{
			System.out.println("Player cannot be created for the given stream and type");
			me.printStackTrace();
		}
	}

	/**
	 * Stop playing the current song
	 */
	public void stop()
	{
		try {
			player.stop();
		} catch (MediaException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Command action
	 * @param c Command calling this function
	 * @param d Current displayable
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == this.cmndBack)
		{
			this.menu.getMainMIDlet().getCurrentDisplay().setCurrent(container);
		}
	}

	/**
	 * Key released event
	 * @param keyCode Key code
	 */
	protected void keyReleased(int keyCode) {
		super.keyReleased(keyCode);
		
		//Check if key was pressed repeatingly
		if (this.isForwardBlue || isRewindBlue)
		{
			//Ignore the release
			this.isForwardBlue = this.isRewindBlue = false;
			//Repaint the canvas
			repaint();
			return;
		}

		switch (keyCode)
		{
			/* Fire */
			case FIRE:
			case (-5):
				try {
					if (player.getState() == player.STARTED)
					{
						songCurrentTime = player.getMediaTime();
						player.stop();
					}
					else
					{
						player.setMediaTime(songCurrentTime);
						player.start();
					}
				} catch (MediaException ex) {
					ex.printStackTrace();
				}
				//Redraw the canvas
				repaint();
				break;
			/* Right */
			case RIGHT:
			case (-4):
				nextSong();
				break;
			/* Left */
			case LEFT:
			case (-3):
				prevSong();
		}
	}

	/**
	 * Key repeated event (For FF or RW)
	 * @param keyCode Key code
	 */
	protected void keyRepeated(int keyCode) {
		super.keyRepeated(keyCode);
		songCurrentTime = player.getMediaTime();
		switch (keyCode)
		{
			//Fast forward
			case RIGHT:
			case (-4):
				this.isForwardBlue = true;
				songCurrentTime = songCurrentTime + songTotalTime / 25;
				try
				{
					//Check that new current time is in range
					if (songCurrentTime < songTotalTime)
						player.setMediaTime( songCurrentTime);
					else
						player.setMediaTime(songTotalTime);
					player.start() ;
				}
				catch( MediaException ex )
				{
					ex.printStackTrace();
				}
				break;
			//Rewind
			case LEFT:
			case (-3):
				this.isRewindBlue = true;
				songCurrentTime = songCurrentTime - songTotalTime / 25;
				try
				{
					//Check that new current time is in range
					if (songCurrentTime > 0)
						player.setMediaTime(songCurrentTime);
					else
						player.setMediaTime( 0 );
					player.start() ;
				}
				catch( MediaException ex )
				{
					ex.printStackTrace();
				}
				break;
		}
		//Repaint the canvas
		repaint();
	}

	/**
	 * Change in player's state
	 * @param player Player
	 * @param event Event caused the calling of this function
	 * @param eventData Event's data
	 */
	public void playerUpdate(Player player, String event, Object eventData) {
		//Check what was changed
		if (event.equals(PlayerListener.END_OF_MEDIA))
		{
			switch (this.menu.settings.getRepeat())
			{
				case Settings.REPEAT_ALL:
					if (this.currSongIndex < (this.songs.length - 1))
						this.changeSong(currSongIndex + 1);
					else
					{
						if (this.menu.settings.getShuffle())
						{
							this.songs.Shuffle();
							this.changeSong(0);
						}
						else
							this.changeSong(0);
					}
					break;
				case Settings.REPEAT_NONE:
					if (this.currSongIndex < (this.songs.length - 1))
						this.changeSong(currSongIndex + 1);
					break;
				case Settings.REPEAT_SONG:
					try {
						this.player.start();
					} catch (MediaException ex) {
						ex.printStackTrace();
					}
					break;
			}
		}
	}

	/**
	 * Next song
	 */
	private void nextSong() {
		currSongIndex++;
		if (currSongIndex >= this.songs.length){
			//Check if shuffle is needed
			if (menu.settings.getShuffle())
				this.songs.Shuffle();
			currSongIndex = 0;
		}
		this.changeSong(currSongIndex);
	}

	/**
	 * Previouse song
	 */
	private void prevSong() {
		currSongIndex--;
		if (currSongIndex < 0)
			currSongIndex = this.songs.length - 1;
		this.changeSong(currSongIndex);
	}

	/**
	 * Split 1 string at the space that is closest to the middle of the string
	 * @param longText String to split
	 * @return String splitted to 2
	 */
	public String[] get2StringsFrom1(String longText)
	{
		String[] stringArr = new String[2];
		char[] charArr = longText.toCharArray();
		int spaceIndex = charArr.length - 1;
		int charIdxDistFrmMid = charArr.length / 2;

		//Find the closes space to the center of string
		for (int i = 0; i < charArr.length; i++)
		{
			if (charArr[i] == ' ')
			{
				if (Math.abs(charArr.length / 2 - i) < charIdxDistFrmMid)
				{
					spaceIndex = i;
					charIdxDistFrmMid = Math.abs(charArr.length / 2 - i);
				}
			}
		}

		//Seperate the strings
		stringArr[0] = String.valueOf(charArr, 0, spaceIndex);
		stringArr[1] = String.valueOf(charArr, spaceIndex + 1, charArr.length - spaceIndex - 1);
		return stringArr;
	}

	/**
	 * Paint function
	 * @param g Graphics component
	 */
	protected void paint(Graphics g) {
		//Draw BG
		g.drawImage(bgImage, 0, 0, 0);
		//Draw buttons
		if (this.player.getState() == this.player.STARTED)
			g.drawImage(this.imgPause,
							this.getWidth() / 2 - this.imgPause.getWidth() / 2,
							this.getHeight() - this.imgPause.getHeight()  - 10,
							0);
		else
			g.drawImage(this.imgPlay,
							this.getWidth() / 2 - this.imgPlay.getWidth() / 2,
							this.getHeight() - this.imgPlay.getHeight()  - 10,
							0);
		if (this.isRewindBlue)
			g.drawImage(this.imgPrevPressed,
							this.getWidth() / 2 - this.imgPlay.getWidth() / 2 -
									this.imgPrev.getWidth() - 5,
							this.getHeight() - this.imgPlay.getHeight()  - 10 +
									(this.imgPlay.getHeight() - this.imgPrev.getHeight()) / 2,
							0);
		else
			g.drawImage(this.imgPrev,
							this.getWidth() / 2 - this.imgPlay.getWidth() / 2 -
									this.imgPrev.getWidth() - 5,
							this.getHeight() - this.imgPlay.getHeight()  - 10 +
									(this.imgPlay.getHeight() - this.imgPrev.getHeight()) / 2,
							0);
		if (this.isForwardBlue)
			g.drawImage(this.imgNextPressed,
							this.getWidth() / 2 + this.imgPlay.getWidth() / 2 + 5,
							this.getHeight() - this.imgPlay.getHeight()  - 10 +
									(this.imgPlay.getHeight() - this.imgNext.getHeight()) / 2,
							0);
		else
			g.drawImage(this.imgNext,
							this.getWidth() / 2 + this.imgPlay.getWidth() / 2 + 5,
							this.getHeight() - this.imgPlay.getHeight()  - 10 +
									(this.imgPlay.getHeight() - this.imgNext.getHeight()) / 2,
							0);
		//Draw song's name
		Song playingSong = this.songs.ValueAt(this.currSongIndex);
		String title = playingSong.getTag().getTitle();
		String artist = "By: " + playingSong.getTag().getArtist();
		g.setFont(songNameFont);

		/* In drawString 20 is equivilent to Graphics.TOP | Graphics.LEFT */
		int artistY;
		//Title is too log
		if (songNameFont.stringWidth(title) > this.getWidth())
		{
			//Seperate the title to 2 lines
			String[] sepTitle = this.get2StringsFrom1(title);
			g.setColor(0x00A3A3A3); // Gray
			g.drawString(sepTitle[0],this.getWidth()/2 -
				songNameFont.stringWidth(sepTitle[0])/2 + 1, 6, 20);
			//Draw the second line's shadow
			g.drawString(sepTitle[1], this.getWidth()/2 -
				songNameFont.stringWidth(sepTitle[1])/2 + 1, songNameFont.getHeight() + 10, 20);
			g.setColor(0x00000000); // Black
			//Draw the first line
			g.drawString(sepTitle[0],this.getWidth()/2 -
				songNameFont.stringWidth(sepTitle[0])/2, 5, 20);
			//Draw the second line
			g.drawString(sepTitle[1], this.getWidth()/2 -
				songNameFont.stringWidth(sepTitle[1])/2, songNameFont.getHeight() + 9, 20);
			//Draw the first line's shadow
			artistY = songNameFont.getHeight()*2 + 13;
		}
		else
		{
			//Draw shadow
			g.setColor(0x00A3A3A3); // Gray
			g.drawString(title, this.getWidth()/2 - songNameFont.stringWidth(title)/2 + 1, 6, 20);
			//Draw the title in the middle top of the screen
			g.setColor(0x00000000); // Black
			g.drawString(title, this.getWidth()/2 - songNameFont.stringWidth(title)/2, 5, 20);
			artistY = songNameFont.getHeight() + 9;
		}

		//Artist is too log
		if (songNameFont.stringWidth(artist) > this.getWidth())
		{
			//Seperate the title to 2 lines
			String[] sepArtist = this.get2StringsFrom1(artist);
			g.setColor(0x00A3A3A3); // Gray
			//Draw the first line's shadow
			g.drawString(sepArtist[0],this.getWidth()/2 -
				songNameFont.stringWidth(sepArtist[0])/2 + 1,
				artistY + 1, 20);
			//Draw the second line's shadow
			g.drawString(sepArtist[1], this.getWidth()/2 -
				songNameFont.stringWidth(sepArtist[1])/2 + 1,
				artistY + songNameFont.getHeight() + 10, 20);
			g.setColor(0x00000000); // Black
			//Draw the first line
			g.drawString(sepArtist[0],this.getWidth()/2 -
				songNameFont.stringWidth(sepArtist[0])/2,
				artistY, 20);
			//Draw the second line
			g.drawString(sepArtist[1], this.getWidth()/2 -
				songNameFont.stringWidth(sepArtist[1])/2,
				artistY + songNameFont.getHeight() + 9, 20);
		}
		else
		{
			//Draw shadow
			g.setColor(0x00A3A3A3); // Gray
			g.drawString(artist, this.getWidth()/2 - songNameFont.stringWidth(artist)/2 + 1,
				artistY + 1, 20);
			g.setColor(0x00000000); // Black
			//Draw the title in the middle top of the screen
			g.drawString(artist, this.getWidth()/2 - songNameFont.stringWidth(artist)/2,
				artistY, 20);
		}
		//Update media time
		this.songCurrentTime = this.player.getMediaTime();
		//Time string
		String timeString = this.getTimeString();
		//Draw song time and progress bar
		int timeStrWidth = songTimeFont.stringWidth(timeString);
		int w = (int) (this.getWidth() * 0.9), h = 8;
		int x = this.getWidth()/2 - w/2,
			y = this.getHeight() - imgPlay.getHeight() - h - songTimeFont.getHeight() - 15;
		//Draw border of progress bar
		g.drawRect(x, y, w, h);
		//Draw time string
		g.setFont(songTimeFont);
		g.drawString(timeString, this.getWidth() / 2 -
			timeStrWidth/2, y + songTimeFont.getHeight() - 3, 20);
		//Set props to inside the rect
		w -= 2; h -= 1; x += 1; y += 1;
		//Set progress line's width
		w *= ((double)this.songCurrentTime / (double)this.songTotalTime);
		g.setColor(0x002211FF); // Kinda blue
		g.fillRect(x, y, w, h);
	}

	/**
	 * Get string indicated the current time of the song and song duration
	 */
	private String getTimeString() {
		//Create time string
		//Set integers
		int curTime = (int)(this.songCurrentTime / 1000000);
		int totTime = (int)(this.songTotalTime / 1000000);
		int curMin = curTime / 60, curSec = curTime % 60;
		int totMin = totTime / 60, totSec = totTime % 60;
		//Set strings
		String curM, curS, cur, totM, totS, tot;
		if (curMin < 10) curM = "0" + curMin;
		else curM = String.valueOf(curMin);
		if (curSec < 10) curS = "0" + curSec;
		else curS = String.valueOf(curSec);
		if (totMin < 10) totM = "0" + totMin;
		else totM = String.valueOf(totMin);
		if (totSec < 10) totS = "0" + totSec;
		else totS = String.valueOf(totSec);
		cur = curM + ":" + curS;
		tot = totM + ":" + totS;
		//Return final result
		return cur + "/" + tot;
	}
}