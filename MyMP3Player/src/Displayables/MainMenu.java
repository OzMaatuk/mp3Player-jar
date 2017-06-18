package Displayables;

import Data_Structures.SongList;
import Data_Structures.AlbumList;
import Data_Structures.ArtistList;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import MainPackage.MyMP3Player;
import Memory_Management.Settings;
import java.io.IOException;
import javax.microedition.media.Player;

/**
 * Main menu - starting point of the application
 * @author
 */
public class MainMenu extends List implements CommandListener {
    private MyMP3Player mainMidlet;
    private Command cmndExit;
    /**
     * Current player that's playing a song
     */
    public Player currentPlayer = null;
    /**
     * Songs list
     */
    public SongList songs = null;
    /**
     * Albums list
     */
    public AlbumList albums = null;
    /**
     * Artists list
     */
    public ArtistList artists = null;
    /**
     * Application settings
     */
    public Settings settings = null;
    /**
     * Default c'tor
     * @param container Midlet containing this class
     */
    public MainMenu(MyMP3Player container)
    {
        super("My MP3 Player", List.IMPLICIT);
        this.mainMidlet = container;
        try {
            this.append("Play All", Image.createImage("/Images/PlayAll.png"));
            this.append("Artists", Image.createImage("/Images/Artist.png"));
            this.append("Albums", Image.createImage("/Images/CD.png"));
            this.append("Playlists", Image.createImage("/Images/MusicDisk.png"));
            this.append("Settings", Image.createImage("/Images/Settings.png"));
            this.append("Update Library", Image.createImage("/Images/BlueTunes.png"));
            this.append("Exit", Image.createImage("/Images/Exit.png"));

        } catch (IOException ex) {
            String msg = ex.getMessage();
            System.out.println(msg);
        }

	//Set exit command & command listener
        cmndExit = new Command("Exit", Command.EXIT, 1);
        this.addCommand(cmndExit);
        this.setCommandListener(this);
    }

    /**
     * Get main midlet
     * @return Main midlet containing this menu
     */
    public MyMP3Player getMainMIDlet()
    {
        return this.mainMidlet;
    }

    /**
     * Command action
     * @param c Command calling this function
     * @param d Currently displayed displayable
     */
    public void commandAction(Command c, Displayable d) {
        if (c == cmndExit)
        {
            mainMidlet.destroyApp(true);
        }
        else if (c == List.SELECT_COMMAND)
        {
            //Check which item was selected
            String selectedItem = this.getString(this.getSelectedIndex());
            changeChoice(selectedItem);
        }
    }

    /**
     * Change chioce and go to it's displayable
     * @param selectedItem New selected item ("Play All" / "Artists", / "Albums" / "Playlists" / "Settings" / "Update Library" / "Exit")
     */
    public void changeChoice(String selectedItem)
    {
            //Change the current display
            if (selectedItem.equals("Play All"))
            {
                PlayAll pa = new PlayAll(this);
                this.mainMidlet.getCurrentDisplay().setCurrent(pa);
            } else if (selectedItem.equals("Artists"))
            {
                Artists a = new Artists(this);
                this.mainMidlet.getCurrentDisplay().setCurrent(a);
            } else if (selectedItem.equals("Albums"))
            {
                Albums a = new Albums(this);
                this.mainMidlet.getCurrentDisplay().setCurrent(a);
            } else if (selectedItem.equals("Playlists"))
            {
                Playlists p = new Playlists(this);
                this.mainMidlet.getCurrentDisplay().setCurrent(p);
            } else if (selectedItem.equals("Settings"))
            {
                ShowSettings s = new ShowSettings(this);
                this.mainMidlet.getCurrentDisplay().setCurrent(s);
            } else if (selectedItem.equals("Update Library"))
            {
                UpdateLibrary ul = new UpdateLibrary(this);
                this.mainMidlet.getCurrentDisplay().setCurrent(ul);
            } else if (selectedItem.equals("Exit"))
            {
                mainMidlet.destroyApp(true);
            }
    }
}
