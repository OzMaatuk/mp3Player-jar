package Memory_Management;
import Data_Structures.Album;
import Data_Structures.AlbumList;
import Data_Structures.Artist;
import Data_Structures.ArtistList;
import Data_Structures.Song;
import Data_Structures.SongList;
import Data_Structures.SongNode;
import Data_Structures.Tag;
import Files_Handler.FileHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.rms.RecordStore;

/**
 * Lists is used to load/save/create Songs, Albums and Artists lists
 * @author
 */
public abstract class Lists {
	/* Saving Functions */
	/**
	 * Save songs list to recordstore
	 * @param songs Songs to save
	 */
	public static void saveSongs(SongList songs)
	{
		/* Save songs list */
		try
		{
		    RecordStore rs = RecordStore.openRecordStore("AllSongsRS", true);

		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    DataOutputStream dos = new DataOutputStream(bos);

		    /* First save the size of the list */
		    dos.writeInt(songs.length);

		    for (int i = 0; i < songs.length; i++)
		    {
			    Song song = songs.ValueAt(i);
			    dos.writeUTF(song.getPath());
			    ByteBuffer buf = song.getTag().toByteBuffer();
			    buf.clear();
			    byte[] bytes = new byte[buf.remaining()];
			    buf.get(bytes);
			    dos.write(bytes);
		    }
		    dos.flush();

		    byte[] recordOut = bos.toByteArray();

		    if (rs.getNumRecords() == 0)
			rs.addRecord(recordOut, 0, recordOut.length);
		    else
			rs.setRecord(1, recordOut, 0, recordOut.length);

		    dos.close();
		    rs.closeRecordStore();
		}
		catch (Exception ex)
		{
		    ex.printStackTrace();
		}
	}

	/**
	 * Save songs list to file (MyMp3PlayerData.dat at the main root dir)
	 * @param songs Songs to save
	 */
	public static void saveSongs2File(SongList songs)
	{
		/* Save songs list */
		try
		{
		    String url = "file:///" + FileHandler.GetDefaultRoot() + "MyMp3PlayerData.dat";
		    FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
		    if (!fconn.exists()) {
			fconn.create();
		    }
		    
		    if (!fconn.exists()) {
			fconn.create();
		    }
		    DataOutputStream dops = fconn.openDataOutputStream();

		    /* Write num of songs */
		    dops.write(songs.length);
		    for (int i = 0; i < songs.length; i++)
		    {
			    Song song = songs.ValueAt(i);
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

	/* Loading Functions */
	/**
	 * Load songs list from recordstore
	 * @return
	 */
	public static SongList loadSongs()
	{
		SongList songs = new SongList();

		/* If info is saved, then get it */
		try
		{
		    RecordStore rs = RecordStore.openRecordStore("AllSongsRS", false);

		    //Check if recordstore is null
		    if (rs == null)
			    return null;

		    byte[] record = rs.getRecord(1);

		    ByteArrayInputStream bis = new ByteArrayInputStream(record);

		    DataInputStream dis = new DataInputStream(bis);

		    /* First read the size of the list */
		    int listLength = dis.read();

		    for (int i = 0; i < listLength; i++)
		    {
			    String filePath = dis.readUTF();
			    byte[] byteArr = new byte[Song.tagSize];
			    dis.read(byteArr);
			    Song song = new Song(filePath,
				    new Tag(ByteBuffer.wrap(byteArr), filePath));
			    songs.SortedAddition(song);
		    }

		    dis.close();
		    rs.closeRecordStore();
		}
		catch (Exception ex)
		{
		    /* Probably not found ...  */
		    System.out.print("ERROR: " + ex.getMessage());
		    return null;
		}
		
		return songs;
	}


    /**
     * Load songs list from file (MyMp3PlayerData.dat at the main root dir)
     * @return Songs list containing all saved songs in MyMp3PlayerData.dat
     */
    public static SongList LoadAllSongsFromFile() {
		try {
		    SongList songs = new SongList();
		    String url = "file:///" + FileHandler.GetDefaultRoot() + "MyMp3PlayerData.dat";
		    FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
		    if (!fconn.exists()) {
			fconn.close();
			return null;
		    }
		    DataInputStream dips = fconn.openDataInputStream();

		    /* Read num of songs */
		    int length = dips.read();
		    for (int i = 0; i < length; i++)
		    {
			    String filePath = dips.readUTF();
			    byte[] byteArr = new byte[Song.tagSize];
			    dips.read(byteArr);
			    Song song = new Song(filePath,
				    new Tag(ByteBuffer.wrap(byteArr), filePath));
			    songs.SortedAddition(song);
		    }

		    dips.close();
		    fconn.close();

		    return songs;
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

	/**
	 * Fill albums and artists according to the information in songs
	 * plus, save the songs in a recordstore for further usage
	 * If songs is null then songs will be loaded from a recordstore
	 * if it exists, and if it isn't then the function won't do a thing
	 * @param songs Songs list to use
	 * @param albums Albums list to fill (If not empty it'll be cleared)
	 * @param artists Artists list to fill (If not empty it'll be cleared)
	 */
	public static void CreateAllLists(SongList songs, AlbumList albums, ArtistList artists)
    {
		//Check if a loading from a recordstore is needed
		if (songs == null) songs = Lists.loadSongs();

		//Check if songs weren't loaded
		if (songs == null) return;

		/* Clear artists and albums */
		albums.DeleteAll();
		artists.DeleteAll();

		//For a faster loop
		SongNode tmpNode = songs.getHead();

		while (tmpNode != null)
		{
			//Save the current song we're working with
			Song s = tmpNode.GetValue();
			//Create an artist and search for him in the
			//already saved artists
			Artist ar = new Artist(s.getTag().getArtist());
			int aIndex = artists.IndexOf(ar);
			//If artists exists then replace the reference
			//to the old one
			if (aIndex >= 0) ar = artists.ValueAt(aIndex);
			else artists.SortedAddition(ar);
			//Do the save with the album
			Album al = new Album(s.getTag().getAlbum(), ar, new SongList());
			aIndex = albums.IndexOf(al);
			//Do the thing with the reference again
			if (aIndex >= 0) al = albums.ValueAt(aIndex);
			else{
				albums.SortedAddition(al);
				ar.getAlbums().SortedAddition(al);
			}
			//Now we have a pointer to the artist and to the album
			//Now add the song to the album and the album to the artist
			al.getSongs().SortedAddition(s);
			//Move to the next artist
			tmpNode = tmpNode.GetNextNode();
		}

		//Now save all songs
		Lists.saveSongs(songs); // To RS
		Lists.saveSongs2File(songs); // To File
		//Finished
    }
}