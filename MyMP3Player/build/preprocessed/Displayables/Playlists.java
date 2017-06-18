package Displayables;
import Data_Structures.Playlist;
import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import Data_Structures.PlaylistList;
import Data_Structures.SongList;
import Files_Handler.FileHandler;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

/**
 * Playlists displayable that shows all playlists and their songs in the device
 * @author
 */
public class Playlists extends List implements CommandListener {
    private Command cmndBack;
    private Command cmndNewPlaylist;
    private Command cmndDelPlaylist;
    private MainMenu mainMenu;
    private PlaylistList playlists;
    private Playlist playlist;
    private SongList songs;
    private Mp3Player mp3Player;
    private String showing;
    /**
     * Default c'tor
     * @param mainMenu Main menu containing this class
     */
    public Playlists(MainMenu mainMenu)
    {
        super("Playlists", List.IMPLICIT);

        this.mainMenu = mainMenu;
	this.playlists = FileHandler.GetAllPlaylists();
	this.showing = "Playlists";

	for (int i = 0; i < this.playlists.length; i++)
		this.append(this.playlists.ValueAt(i).getName(), null);

        cmndBack = new Command("Back", Command.CANCEL, 1);
	cmndNewPlaylist = new Command("New Playlist", Command.OK, 2);
	cmndDelPlaylist = new Command("Delete Playlist", Command.OK, 3);

        this.addCommand(cmndBack);
        this.addCommand(cmndNewPlaylist);
        this.addCommand(cmndDelPlaylist);
        
        this.setCommandListener(this);
    }

    /**
     * Command action
     * @param c Command calling this function
     * @param d Displayable currently displayed
     */
    public void commandAction(Command c, Displayable d) {
	if (c == cmndBack)
        {
		if (this.showing.equals("Playlists"))
		{
			//Return to main menu
			this.mainMenu.getMainMIDlet().returnToMainMenu();
		}
		else if (this.showing.equals("Songs"))
		{
			//Return to show albums
			this.setTitle("Playlists");
			this.deleteAll();
			for (int i = 0; i < playlists.length; i++)
				this.append(playlists.ValueAt(i).getName(), null);
			this.setSelectedIndex(this.playlists.IndexOf(playlist), true);
			this.showing = "Playlists";
		}
        } else if (c == List.SELECT_COMMAND)
        {
		if (this.showing.equals("Playlists"))
		{
			//Show album songs
			this.playlist = this.playlists.ValueAt(this.getSelectedIndex());
			this.deleteAll();
			this.setTitle(playlist.getName());
			this.songs = playlist.getSongs();
			for (int i = 0; i < songs.length; i++)
				this.append(songs.ValueAt(i).getTag().getTitle(), null);
			this.showing = "Songs";
		}
		else if (this.showing.equals("Songs"))
		{
			mp3Player = new Mp3Player(mainMenu, this, songs, this.getSelectedIndex());
			this.mainMenu.getMainMIDlet().getCurrentDisplay().setCurrent(mp3Player);
		}
        } else if (c == cmndNewPlaylist)
	{
		new PlaylistCreate(this);
	} else if (c == cmndDelPlaylist)
	{
		//Delete selcted playlist
		Playlist toDelete = this.playlists.ValueAt(this.getSelectedIndex());
		try {
			FileConnection fc = (FileConnection) Connector.open(toDelete.getPath(), Connector.READ_WRITE);
			fc.delete();
			this.delete(this.getSelectedIndex());
		} catch (IOException ex) {
			ex.printStackTrace();
			Alert alert = new Alert("Error!", "Can't Delete File!", null, AlertType.ERROR);
			alert.setTimeout(2000);
			this.mainMenu.getMainMIDlet().getCurrentDisplay().setCurrent(alert, this);
		}
	}
    }

    /**
     * Get the main menu containing this class
     * @return The main menu
     */
    public MainMenu getMainMenu() {
		return mainMenu;
	}

	/**
	 * List of all playlsits
	 * @return All playlists
	 */
	public PlaylistList getPlaylists() {
		return playlists;
	}
	
}