package Memory_Management;

import Data_Structures.SongList;
import Displayables.UpdateLibrary;
import Files_Handler.FileHandler;

/**
 * Library updater, reading all files in the device
 * @author
 */
public class LibraryUpdater implements Runnable {
	private UpdateLibrary container;
	/**
	 * Default c'tor
	 * @param container UpdateLibrary instance containing this LibraryUpdater
	 */
	public LibraryUpdater(UpdateLibrary container)
	{
		super();
		this.container = container;
	}
	/**
	 * run function for thread start
	 */
	public void run() {
		SongList songs = FileHandler.GetAllMp3sList();
		this.container.Finish(songs);
	}
}