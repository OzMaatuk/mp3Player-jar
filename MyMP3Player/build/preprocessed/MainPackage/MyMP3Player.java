package MainPackage;

import Data_Structures.AlbumList;
import Data_Structures.ArtistList;
import Displayables.MainMenu;
import Memory_Management.Lists;
import Memory_Management.Settings;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * Main MIDlet that runs the main menu
 * @author
 */
public class MyMP3Player extends MIDlet {
    Display currentDisplay;
    MainMenu mm;
    /**
     * Start running (create main menu and run it)
     */
    public void startApp() {
        currentDisplay = Display.getDisplay(this);
        mm = new MainMenu(this);

	//Set songs list for the main menu
	mm.songs = Lists.loadSongs();
	//Try loading from file if rs doesn't exist
	if (mm.songs == null) mm.songs = Lists.LoadAllSongsFromFile();

	//Set settings for main menu
	mm.settings = Settings.LoadSetting();
	if (mm.settings == null) {
		mm.settings = new Settings();
		Settings.SaveSettings(mm.settings);
	}
	
	if (mm.songs != null)
	{
		mm.albums = new AlbumList();
		mm.artists = new ArtistList();
		//Fill songs, artists and albums lists
		Lists.CreateAllLists(mm.songs, mm.albums, mm.artists);

		currentDisplay.setCurrent(mm);
	}
	else
		mm.changeChoice("Update Library");
    }

    /**
     * No pause available
     */
    public void pauseApp() {
    }

    /**
     * Destroy the application
     * @param unconditional not in use
     */
    public void destroyApp(boolean unconditional) {
        this.notifyDestroyed();
    }

    /**
     * Get the current display
     * @return Current display
     */
    public Display getCurrentDisplay()
    {
        return this.currentDisplay;
    }

    /**
     * Set current display to main menu
     */
    public void returnToMainMenu()
    {
        this.currentDisplay.setCurrent(mm);
    }
}
