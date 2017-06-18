package Memory_Management;

import Files_Handler.FileHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.rms.RecordStore;

/**
 * Settings object indicating volume, repeat and shuffle
 * @author
 */
public class Settings {

	/* Static Properties */
	/**
	 * Don't repeat the list of songs
	 */
	public static final int REPEAT_NONE = 0;
	/**
	 * Repeat the list of songs
	 */
	public static final int REPEAT_ALL = 1;
	/**
	 * Repeat one song over and over
	 */
	public static final int REPEAT_SONG = 2;

	/* Private Properties */
	private int volume;
	private boolean shuffle;
	private int repeat;

	/**
	 * Default C'tor
	 */
	public Settings()
	{
		this.volume = 100;
		this.repeat = REPEAT_NONE;
		this.shuffle = false;
	}

	/**
	 * Save settings to recordstore and file
	 * @param setts Settings to save
	 */
	public static void SaveSettings(Settings setts)
	{
		/* Save to recordstore */
		try
		{
		    RecordStore rs = RecordStore.openRecordStore("SettingsRS", true);

		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    DataOutputStream dos = new DataOutputStream(bos);

		    dos.write(setts.getVolume());
		    dos.write(setts.getRepeat());
		    dos.writeBoolean(setts.getShuffle());

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

		/* Save to file */
		try
		{
		    String url = "file:///" + FileHandler.GetDefaultRoot() + "MyMp3PlayerSettings.dat";
		    FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
		    if (!fconn.exists()) {
			fconn.create();
		    }

		    if (!fconn.exists()) {
			fconn.create();
		    }
		    DataOutputStream dops = fconn.openDataOutputStream();

		    dops.write(setts.getVolume());
		    dops.write(setts.getRepeat());
		    dops.writeBoolean(setts.getShuffle());

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
	 * Load settings from recordstore (and if there isn't then try loading from file)
	 * @return Saved settings, or null if there aren't any
	 */
	public static Settings LoadSetting()
	{
		Settings setts = new Settings();

		/* If info is saved, then get it */
		try
		{
		    RecordStore rs = RecordStore.openRecordStore("SettingsRS", false);

		    //Check if recordstore is null
		    if (rs != null)
		    {
			    byte[] record = rs.getRecord(1);

			    ByteArrayInputStream bis = new ByteArrayInputStream(record);

			    DataInputStream dis = new DataInputStream(bis);

			    setts.setVolume(dis.read());
			    setts.setRepeat(dis.read());
			    setts.setShuffle(dis.readBoolean());

			    dis.close();
			    rs.closeRecordStore();

			    return setts;
		    }
		}
		catch (Exception ex)
		{
		    /* Probably not found ...  */
		    System.out.print("ERROR: " + ex.getMessage());
		}

		/* Load the settings from file */
		try {
		    String url = "file:///" + FileHandler.GetDefaultRoot() + "MyMp3PlayerSettings.dat";
		    FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
		    if (!fconn.exists()) {
			fconn.close();
			return null;
		    }
		    DataInputStream dips = fconn.openDataInputStream();

		    /* Read settings */
		    setts.setVolume(dips.read());
		    setts.setRepeat(dips.read());
		    setts.setShuffle(dips.readBoolean());

		    dips.close();
		    fconn.close();

		    return setts;
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

	/* Getters & Setters */
	/**
	 * Get volume
	 * @return Volume level (0 - 100)
	 */
	public int getVolume()
	{
		return this.volume;
	}
	/**
	 * Set volume level
	 * @param volume Volume level (0 - 100)
	 */
	public void setVolume(int volume)
	{
		this.volume = volume;
	}

	/**
	 * Get repeat indicator
	 * @return One of the finals indicating repeat (REPEAT_ALL, REPEAT_NONE, REPEAT_SONG)
	 */
	public int getRepeat() {
		return repeat;
	}
	/**
	 * Set the repeat value
	 * @param repeat One of the finals indicating repeat (REPEAT_ALL, REPEAT_NONE, REPEAT_SONG)
	 */
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	/**
	 * Get a boolean indicating if shuffle is turned on
	 * @return Is shuffle on
	 */
	public boolean getShuffle()
	{
		return this.shuffle;
	}
	/**
	 * Set shuffle to on / off
	 * @param shuffle True to turn on the shuffle or false to turn it off
	 */
	public void setShuffle(boolean shuffle)
	{
		this.shuffle = shuffle;
	}
}
