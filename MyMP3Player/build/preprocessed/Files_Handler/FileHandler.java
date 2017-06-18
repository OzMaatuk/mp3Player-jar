package Files_Handler;
import Data_Structures.PlaylistList;
import Data_Structures.Song;
import Data_Structures.SongList;
import Data_Structures.StringList;
import Displayables.PlaylistCreate;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.*;

/**
 * FileHandler is used to read and write file and directories in the device
 * @author
 */
public abstract class FileHandler {
	/**
	 * Get a string array of all roots
	 * (Not including the doubled roots like in nokia when it has both name
	 *  e.g. "d/" and "Memory Card/"
	 * @return Array of roots as strings
	 */
	public static String[] GetRootListing()
    {
            Enumeration drives = FileSystemRegistry.listRoots();
            StringList rootList = new StringList();
            while (drives.hasMoreElements()) {
                String driveString = drives.nextElement().toString();
		if (driveString.length() < 5) /* To get c/ or e/ and not duplicates with full names*/
	                rootList.Add(driveString);
            }
            return rootList.ToArray();
    }

	/**
	 * Get the first root in the roots list
	 * @return Main root directory
	 */
	public static String GetDefaultRoot()
    {
            Enumeration drives = FileSystemRegistry.listRoots();
            if (drives.hasMoreElements())
		    return drives.nextElement().toString();
	    else
		    return "";
    }

	/**
	 * Get a string list of all roots
	 * (Not including the doubled roots like in nokia when it has both name
	 *  e.g. "d/" and "Memory Card/"
	 * @return All roots as a StringList
	 */
	public static StringList GetRootListListing()
    {
            Enumeration drives = FileSystemRegistry.listRoots();
            StringList rootList = new StringList();
            while (drives.hasMoreElements()) {
                String driveString = drives.nextElement().toString();
		if (driveString.length() < 5) /* To get c/ or e/ and not duplicates with full names*/
	                rootList.Add("file:///" + driveString);
            }
            return rootList;
    }

    /**
     * Get all playlists paths in the main root directory
     * @return Paths of all playlists saved on the device
     */
    public static String[] GetPlaylistsPaths()
    {
	    //Initialize list 2 return
	    StringList playlists = new StringList();
	    //Get default root
	    String path = FileHandler.GetDefaultRoot();try {
            //Opens a file connection in READ mode
            path = "file:///" + path;
            FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
            Enumeration filelist = fc.list("*.mmpp", false); //also hidden files are shown
            String filename;
            while(filelist.hasMoreElements()) {
                filename = (String) filelist.nextElement();
                playlists.Add(path + filename);
            }
            fc.close();
	    //Return playlists path's list
	    return playlists.ToArray();
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return new String[0];
        }
    }

    /**
     * Get all playlists paths in the main root directory
     * @return Paths of all playlists saved on the device
     */
    public static PlaylistList GetAllPlaylists()
    {
	    //Initialize list 2 return
	    PlaylistList playlists = new PlaylistList();
	    //Get default root
	    String path = FileHandler.GetDefaultRoot();try {
            //Opens a file connection in READ mode
            path = "file:///" + path;
            FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
            Enumeration filelist = fc.list("*.mmpp", false); //also hidden files are shown
            String filename;
            while(filelist.hasMoreElements()) {
                filename = (String) filelist.nextElement();
                playlists.Add(PlaylistCreate.LoadPlaylistFromFile(path + filename));
            }
            fc.close();
	    //Return playlists path's list
	    return playlists;
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return null;
        }
    }


    /**
     * Get all directories paths in the given directory
     * @param path Directory to list
     * @return All directories paths in path
     */
    public static String[] GetDirectoryListing(String path) {
        try {
            //Opens a file connection in READ mode
            StringList allDirs = new StringList();
            path = "file:///" + path;
            FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
            Enumeration filelist = fc.list("*", true); //also hidden files are shown
            String filename;
            while(filelist.hasMoreElements()) {
                filename = (String) filelist.nextElement();
                fc = (FileConnection)Connector.open(path + filename, Connector.READ);
                if(fc.isDirectory()) { //checks if fc is a directory
                    //long size = fc.directorySize(false);
                    allDirs.Add(filename);
                } else { //otherwise, is a file
                    //long size = fc.fileSize();
                    //allDirs.Add(filename); - we don't need to show files yet...
                }
            }
            fc.close();
            return allDirs.ToArray();
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return new String[0];
        }
    }

    /**
     * Get all mp3 paths in the given path
     * @param path Directory to list
     * @return All mp3s paths in path
     */
    public static Song[] GetMp3Listing(String path) {
        try {
            //Opens a file connection in READ mode
            SongList allSongs = new SongList();
            path = "file:///" + path;
            FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
            Enumeration filelist = fc.list("*.mp3", true); //also hidden files are shown
            String filename;
            while(filelist.hasMoreElements()) {
                filename = (String) filelist.nextElement();
                allSongs.Add(new Song(path + filename));
            }
            fc.close();
            return allSongs.ToArray();
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return new Song[0];
        }
    }

    /**
     * Add all songs in dirs (and it's sub directories) into mp3s
     * @param dirs Directories list to scan
     * @param mp3s Song list to add the songs to
     */
    public static void ListMp3s(StringList dirs, SongList mp3s)
    {
        try {
		if (dirs.length == 0)
			return; // Finish
		//Opens a file connection in READ mode
		    StringList subDirs = new StringList();
		    for (int i = 0; i < dirs.length; i++)
		    {
			    String path = dirs.ValueAt(i);
			    FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
			    Enumeration filelist = fc.list("*", true); //also hidden files are shown
			    String filename;
			    while(filelist.hasMoreElements()) {
				filename = (String) filelist.nextElement();
				fc = (FileConnection)Connector.open(path + filename, Connector.READ);
				if(fc.isDirectory()) { //checks if fc is a directory
				    subDirs.Add(path + filename);
				} else { //otherwise, it's a file
					if (filename.toLowerCase().endsWith("mp3"))
						 mp3s.Add(new Song(path + filename));
				}
			    }
			    fc.close();
		    }
		    ListMp3s(subDirs, mp3s);
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    /**
     * Get an array of strings indicating the paths of all mp3s in the device
     * @return Paths of all mp3s in the device
     */
    public static Song[] GetAllMp3s()
    {
	    SongList allMp3s = new SongList();
	    StringList roots = FileHandler.GetRootListListing();
	    ListMp3s(roots, allMp3s);
	    return allMp3s.ToArray();
    }

    /**
     * Get a string list of paths of all mp3s in the device
     * @return Paths of all mp3s in the device
     */
    public static SongList GetAllMp3sList()
    {
	    SongList allMp3s = new SongList();
	    StringList roots = FileHandler.GetRootListListing();
	    ListMp3s(roots, allMp3s);
	    return allMp3s;
    }
}