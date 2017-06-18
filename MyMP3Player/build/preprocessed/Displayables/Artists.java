package Displayables;
import Data_Structures.Album;
import Data_Structures.AlbumList;
import Data_Structures.Artist;
import Data_Structures.ArtistList;
import Data_Structures.SongList;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * Show all artists and their albums and their songs
 * @author
 */
public class Artists extends List implements CommandListener {
    private Command cmndBack;
    private MainMenu mainMenu;
    private Artist artist;
    private ArtistList artists;
    private Album album;
    private AlbumList albums;
    private SongList songs;
    private String showing;
    /**
     * Default c'tor
     * @param mainMenu Main menu containing this class
     */
    public Artists(MainMenu mainMenu)
    {
        super("Artists", List.IMPLICIT);

        this.mainMenu = mainMenu;
	this.artists = mainMenu.artists;
	this.showing = "Artists";

	for (int i = 0; i < this.artists.length; i++)
	{
		this.append(this.artists.ValueAt(i).getName(), null);
	}

	//Add commands
        cmndBack = new Command("Back", Command.CANCEL, 1);
        this.addCommand(cmndBack);
        //Set listener
        this.setCommandListener(this);
    }

    /**
     * Command action
     * @param c Command calling this function
     * @param d Currently displayed displayable
     */
    public void commandAction(Command c, Displayable d) {
        if (c == cmndBack)
        {
		if (this.showing.equals("Artists"))
		{
			//Return to main menu
			this.mainMenu.getMainMIDlet().returnToMainMenu();
		}
		else if (this.showing.equals("Albums"))
		{
			//Return to show artists
			this.setTitle("Artists");
			this.deleteAll();
			for (int i = 0; i < artists.length; i++)
				this.append(artists.ValueAt(i).getName(), null);
			this.setSelectedIndex(this.artists.IndexOf(artist), true);
			this.showing = "Artists";
		}
		else if (this.showing.equals("Songs"))
		{
			//Return to show albums
			this.setTitle(this.artist.getName());
			this.deleteAll();
			this.albums = artist.getAlbums();
			for (int i = 0; i < albums.length; i++)
				this.append(albums.ValueAt(i).getName(), null);
			this.setSelectedIndex(this.albums.IndexOf(album), true);
			this.showing = "Albums";
		}
        } else if (c == List.SELECT_COMMAND)
        {
		if (this.showing.equals("Artists"))
		{
			//Show artist albums
			this.artist = this.artists.ValueAt(this.getSelectedIndex());
			this.deleteAll();
			this.setTitle(artist.getName());
			this.albums = artist.getAlbums();
			for (int i = 0; i < albums.length; i++)
				this.append(albums.ValueAt(i).getName(), null);
			this.showing = "Albums";
		}
		else if (this.showing.equals("Albums"))
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