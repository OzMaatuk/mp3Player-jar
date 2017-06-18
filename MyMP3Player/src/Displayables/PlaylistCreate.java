package Displayables;

import Data_Structures.Playlist;
import Data_Structures.Song;
import Data_Structures.SongList;
import Data_Structures.Tag;
import Files_Handler.FileHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 * Create a new playlist
 * @author
 */
public class PlaylistCreate extends List implements CommandListener {
	private Playlists container;
	private SongList songs;
	private Command cmndBack;
	private Command cmndCreate;
	private Command cmndOK;
	private String name;
	private TextBox txtBox;
	/**
	 * Default c'tor
	 * @param container Playlists item containing this class
	 */
	public PlaylistCreate(Playlists container)
	{
		super("Create Playlist", List.MULTIPLE);

		this.container = container;
		this.songs = container.getMainMenu().songs;

		cmndBack = new Command("Back", Command.CANCEL, 1);
		cmndCreate = new Command("Create", Command.OK, 2);
		cmndOK = new Command("Done", Command.OK, 2);
		
		//Firstly, get a name for the playlist
		txtBox = new TextBox("Playlist Name:", "", 20, TextField.ANY);
		this.container.getMainMenu().getMainMIDlet().getCurrentDisplay().setCurrent(txtBox);
		txtBox.addCommand(cmndBack);
		txtBox.addCommand(cmndOK);
		txtBox.setCommandListener(this);
		

		for (int i = 0; i < this.songs.length; i++)
				this.append(this.songs.ValueAt(i).getTag().getTitle(), null);

		this.addCommand(cmndBack);
		this.addCommand(cmndCreate);
		this.setCommandListener(this);
	}

	/**
	 * Command action
	 * @param c Command calling this function
	 * @param d Currently displayed displayable
	 */
	public void commandAction(Command c, Displayable d) {
		if (d == this.txtBox)
		{
			if (c == this.cmndBack)
			{
				if (this.txtBox.getString().length() == 0)
					this.container.getMainMenu().getMainMIDlet().getCurrentDisplay().setCurrent(this.container);
				else
				{
					this.container.getMainMenu().getMainMIDlet().getCurrentDisplay().setCurrent(this);
					this.name = this.txtBox.getString();
				}
			} else if (c == this.cmndOK)
			{
				this.container.getMainMenu().getMainMIDlet().getCurrentDisplay().setCurrent(this);
				this.name = this.txtBox.getString();
			}
		}
		else
		{
			if (c == this.cmndBack)
			{
				this.container.getMainMenu().getMainMIDlet().getCurrentDisplay().setCurrent(this.container);
			} else if (c == this.cmndCreate)
			{
				Playlist playlist = new Playlist(name, FileHandler.GetDefaultRoot() + name + ".mmpp");
				SongList selectedSongs = new SongList();
				boolean[] selectedArray = new boolean[this.songs.length];
				this.getSelectedFlags(selectedArray);
				for (int i = 0; i < this.songs.length; i++)
					if (selectedArray[i])
						selectedSongs.Add(this.songs.ValueAt(i));
				playlist.setSongs(selectedSongs);
				/* Save to file */
				SavePlaylist2File(playlist.getPath(), playlist);
				/* Add to container */
				if (this.container.getPlaylists().IndexOf(playlist) >= 0)
				{
					int index = this.container.getPlaylists().IndexOf(playlist);
					this.container.getPlaylists().RemoveAt(index);
					this.container.delete(index);
				}
				this.container.getPlaylists().Add(playlist);
				this.container.append(playlist.getName(), null);
				//Save playlist
				this.container.getMainMenu().getMainMIDlet().getCurrentDisplay().setCurrent(this.container);
			}
		}
	}

	/* Playlist file handling */
	/**
	 * Save a playlist to file
	 * @param fullPath Path of the file to save to
	 * @param playlist Playlist to save
	 */
	public static void SavePlaylist2File(String fullPath, Playlist playlist) {
		try {
		    String url = "file:///" + fullPath;
		    FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
		    if (!fconn.exists()) {
			fconn.create();
		    }
		    DataOutputStream dops = fconn.openDataOutputStream();

		    /* Write playlist's name */
		    dops.writeUTF(playlist.getName());

		    SongList playlistSongs = playlist.getSongs();
		    /* Write num of songs */
		    dops.write(playlistSongs.length);
		    for (int i = 0; i < playlistSongs.length; i++)
		    {
			    Song song = playlistSongs.ValueAt(i);
			    dops.writeUTF(song.getPath());
			    ByteBuffer buf = song.getTag().toByteBuffer();
			    buf.clear();
			    byte[] bytes = new byte[buf.remaining()];
			    buf.get(bytes);
			    dops.write(bytes);
		    }
		    
		    dops.close();
		    fconn.close();
		}
		catch (IOException ioe) {
		    System.out.println("IOException: "+ioe.getMessage());
		}
		catch (SecurityException se) {
		    System.out.println("Security exception:" + se.getMessage());
		}
	}
	/**
	 * Load playlist from file
	 * @param fullPath Path of file
	 * @return Loaded playlist
	 */
	public static Playlist LoadPlaylistFromFile(String fullPath) {
		try {
			Playlist playlist;
		    String url = fullPath;
		    FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
		    if (!fconn.exists()) {
			fconn.create();
		    }
		    DataInputStream dips = fconn.openDataInputStream();

		    /* Read playlist's name */
		    playlist = new Playlist(dips.readUTF(), url);

		    SongList playlistSongs = new SongList();
		    /* Read num of songs */
		    int length = dips.read();
		    for (int i = 0; i < length; i++)
		    {
			    String filePath = dips.readUTF();
			    byte[] byteArr = new byte[Song.tagSize];
			    dips.read(byteArr);
			    Song song = new Song(filePath,
				    new Tag(ByteBuffer.wrap(byteArr), filePath));
			    playlistSongs.Add(song);
		    }

		    playlist.setSongs(playlistSongs);

		    dips.close();
		    fconn.close();

		    return playlist;
		}
		catch (IOException ioe) {
		    System.out.println("IOException: "+ioe.getMessage());
		}
		catch (SecurityException se) {
		    System.out.println("Security exception:" + se.getMessage());
		}

		/* Failed */
		return null;
	}
}
