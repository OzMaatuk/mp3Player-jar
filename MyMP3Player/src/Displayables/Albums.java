package Displayables;
import Data_Structures.Album;
import Data_Structures.AlbumList;
import Data_Structures.SongList;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * Albums displayable
 * @author
 */
public class Albums extends List implements CommandListener {
    private Command cmndBack;
    private MainMenu mainMenu;
    private AlbumList albums;
    private Album album;
    private SongList songs;
    private String showing;
    /**
     * Display all albums and their songs
     * @param mainMenu Main menu containing this class
     */
    public Albums(MainMenu mainMenu)
    {
        super("Albums", List.IMPLICIT);

        this.mainMenu = mainMenu;
	this.albums = mainMenu.albums;
	this.showing = "Albums";

	for (int i = 0; i < this.albums.length; i++) {
		Album al = this.albums.ValueAt(i);
		this.append(al.getName() + " (" + al.getArtist().getInitials() + ")", null);
	}

	//Set back command
        cmndBack = new Command("Back", Command.CANCEL, 1);
        this.addCommand(cmndBack);
        this.setCommandListener(this);
    }

    /**
     * Command action
     * @param c Command calling this function
     * @param d Current displayable
     */
    public void commandAction(Command c, Displayable d) {
        if (c == cmndBack)
        {
		if (this.showing.equals("Albums"))
		{
			//Return to main menu
			this.mainMenu.getMainMIDlet().returnToMainMenu();
		}
		else if (this.showing.equals("Songs"))
		{
			//Return to show albums
			this.setTitle("Albums");
			this.deleteAll();
			for (int i = 0; i < this.albums.length; i++) {
				Album al = this.albums.ValueAt(i);
				this.append(al.getName() + " (" + al.getArtist().getInitials() + ")", null);
			}
			this.setSelectedIndex(this.albums.IndexOf(album), true);
			this.showing = "Albums";
		}
        } else if (c == List.SELECT_COMMAND)
        {
		if (this.showing.equals("Albums"))
		{
			//Show album songs
			this.album = this.albums.ValueAt(this.getSelectedIndex());
			this.deleteAll();
			this.setTitle(album.getName());
			this.songs = album.getSongs();
			for (int i = 0; i < songs.length; i++)
				this.append(songs.ValueAt(i).getTag().getTitle(), null);
			this.showing = "Songs";
		}
		else if (this.showing.equals("Songs"))
		{
			Mp3Player mp3Player = new Mp3Player(mainMenu, this, songs, this.getSelectedIndex());
			this.mainMenu.getMainMIDlet().getCurrentDisplay().setCurrent(mp3Player);
		}
        }
    }
}