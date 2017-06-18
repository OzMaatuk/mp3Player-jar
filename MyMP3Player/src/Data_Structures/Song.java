package Data_Structures;

import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 * Song class
 * @author
 */
public class Song {
	/**
	 * ID3 tag size
	 */
	public static int tagSize = 128;
	private Tag tag;
	private String filePath;
	/**
	 * Default c'tor
	 * @param filepath Path of song
	 */
	public Song(String filepath)
	{
		this.filePath = filepath;
		//Read file tags
		try {
			FileConnection fc = (FileConnection) Connector.open(filepath);
			if (fc.canRead())
				tag = readTag(fc.openInputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * C'tor with FileConnection to read the tag and path from
	 * @param fc FileConnection that contains that song's file
	 */
	public Song(FileConnection fc)
	{
		this.filePath = fc.getPath();
		//Read file tags
		try {
			if (fc.canRead())
				tag = readTag(fc.openInputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * C'tor containing the file path and tag information
	 * @param filepath Song's path
	 * @param tag Tag information
	 */
	public Song(String filepath, Tag tag)
	{
		this.filePath = filepath;
		this.tag = tag;
	}

	/**
	 * Read tag information from the file
	 * @param in InputStream of the file
	 * @return Tag read
	 * @throws Exception
	 */
	private Tag readTag(InputStream in) throws Exception {
		byte[] tagData = new byte[tagSize];
		int pos = 0;
		for (int curVal = 1; (curVal = (in.read())) >= 0;) {
			tagData[pos++] = (byte)curVal;
			if(pos==tagSize){
				pos=0;
			}
		}
		ByteBuffer bBuf = ByteBuffer.allocateDirect(tagSize);
		bBuf.put(tagData,pos,tagSize-pos);
		bBuf.put(tagData,0,pos);
		bBuf.rewind();
		Tag tg = new Tag(bBuf, this.filePath);
		return tg;
	}

	/**
	 * Does this song equals another one? (Same file path)
	 * @param s The song to compare to
	 * @return True - if they're equal, False otherwise
	 */
	public boolean equals(Song s)
	{
		return (this.filePath.equals(s.getPath()));
	}

	//Getters & Setters
	/**
	 * Get song tag
	 * @return Song tag
	 */
	public Tag getTag()
	{
		if (this.tag != null)
			return this.tag;
		else
		{
			//Create a tag
			this.tag = new Tag(this.filePath);
			this.tag.setAlbum("Unknown");
			this.tag.setArtist("Unknown");
			this.tag.setComment("");
			this.tag.setGenre((byte)Tag.genres.length);
			this.tag.setYear("");
			try {
				this.tag.setTitle(filePath.substring(filePath.lastIndexOf('/'), filePath.indexOf('.')));
			} catch(Exception ex) { this.tag.setTitle("Unknown"); ex.printStackTrace();}
			return this.tag;
		}
	}
	/**
	 * Get the song's file path
	 * @return Song's path
	 */
	public String getPath()
	{
		return this.filePath;
	}

	/**
	 * ToString override
	 * @return Song's title (File's name if there's no title)
	 */
	public String toString() {
		return this.getTag().getTitle();
	}

}