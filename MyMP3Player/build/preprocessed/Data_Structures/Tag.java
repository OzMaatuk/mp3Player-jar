package Data_Structures;

import java.nio.ByteBuffer;

/**
 * Tag information of a certain song
 * @author
 */
public class Tag {
        private String title;
        private String artist;
        private String album;
        private String year;
        private String comment;
        private byte genre;
	private String filePath;
	private ByteBuffer tagValue = null;
	/**
	 * All possible genres (Index of the genre indicates the value of tag in ID3)
	 */
	public static String[] genres =   {   "Blues","Classic Rock","Country","Dance","Disco","Funk","Grunge",
											 "Hip-Hop","Jazz","Metal","New Age","Oldies","Other","Pop","R&B",
											 "Rap","Reggae","Rock","Techno","Industrial","Alternative","Ska",
											 "Death Metal","Pranks","Soundtrack","Euro-Techno","Ambient",
											 "Trip-Hop","Vocal","Jazz+Funk","Fusion","Trance","Classical",
											 "Instrumental","Acid","House","Game","Sound Clip","Gospel",
											 "Noise","AlternRock","Bass","Soul","Punk","Space","Meditative",
											 "Instrumental Pop","Instrumental Rock","Ethnic","Gothic",
											 "Darkwave","Techno-Industrial","Electronic","Pop-Folk",
											 "Eurodance","Dream","Southern Rock","Comedy","Cult","Gangsta",
											 "Top 40","Christian Rap","Pop/Funk","Jungle","Native American",
											 "Cabaret","New Wave","Psychadelic","Rave","Showtunes","Trailer",
											 "Lo-Fi","Tribal","Acid Punk","Acid Jazz","Polka","Retro",
											 "Musical","Rock & Roll","Hard Rock","Folk","Folk-Rock",
											 "National Folk","Swing","Fast Fusion","Bebob","Latin","Revival",
											 "Celtic","Bluegrass","Avantgarde","Gothic Rock","Progressive Rock",
											 "Psychedelic Rock","Symphonic Rock","Slow Rock","Big Band",
											 "Chorus","Easy Listening","Acoustic","Humour","Speech","Chanson",
											 "Opera","Chamber Music","Sonata","Symphony","Booty Bass","Primus",
											 "Porn Groove","Satire","Slow Jam","Club","Tango","Samba",
											 "Folklore","Ballad","Power Ballad","Rhythmic Soul","Freestyle",
											 "Duet","Punk Rock","Drum Solo","Acapella","Euro-House","Dance Hall"};

	/**
	 * Default c'tor
	 * @param filePath Song's path
	 */
	public Tag(String filePath)
	{
		this.title = this.artist = this.album = this.year = this.comment = "Unknown";
		this.genre = (byte) Tag.genres.length;
		this.filePath = filePath;
	}
	/**
	 * C'tor containing both the song's path and a ByteBuffer containing the tag info.
	 * @param bBuf Tag information from 128 last byes of the file
	 * @param filePath Song's path
	 */
	public Tag(ByteBuffer bBuf, String filePath)
	{
		this.filePath = filePath;
		this.populateTag(bBuf);
	}

	/**
	 * Fill all information from a byte buffer into all of this class properties
	 * @param bBuf
	 */
	public void populateTag(ByteBuffer bBuf) {
		this.tagValue = bBuf;
		byte[] tag = new byte[3];
		byte[] tagTitle = new byte[30];
		byte[] tagArtist = new byte[30];
		byte[] tagAlbum = new byte[30];
		byte[] tagYear = new byte[4];
		byte[] tagComment = new byte[30];
		byte[] tagGenre = new byte[1];
		bBuf.get(tag).get(tagTitle).get(tagArtist).get(tagAlbum)
				.get(tagYear).get(tagComment).get(tagGenre);
		if(!"TAG".equals(new String(tag))){
			this.setAlbum("Unknown");
			this.setArtist("Unknown");
			this.setComment("");
			this.setGenre((byte)Tag.genres.length);
			this.setYear("");
			try {
				this.setTitle(this.filePath.substring(filePath.lastIndexOf('/') + 1, filePath.indexOf('.')));
			} catch(Exception ex) { this.setTitle("Unknown"); ex.printStackTrace();}
			return;
		}
		this.setTitle(new String(tagTitle).trim());
		this.setArtist(new String(tagArtist).trim());
		this.setAlbum(new String(tagAlbum).trim());
		this.setYear(new String(tagYear).trim());
		this.setComment(new String(tagComment).trim());
		this.setGenre(tagGenre[0]);
	}

	/**
	 * Get album string
	 * @return Album name
	 */
	public String getAlbum() {
		return album;
	}
	/**
	 * Set album string
	 * @param album Album name
	 */
	public void setAlbum(String album) {
		this.album = album.trim();
		//Check for empty string
		if (this.album.equals(""))
			this.album = "Unknown";
	}
	/**
	 * Get artist string
	 * @return Artist name
	 */
	public String getArtist() {
		return artist;
	}
	/**
	 * Set artist string
	 * @param artist Artist name
	 */
	public void setArtist(String artist) {
		this.artist = artist.trim();
		//Check for empty string
		if (this.artist.equals(""))
			this.artist = "Unknown";
	}
	/**
	 * Get comment string
	 * @return song comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * Set song comment
	 * @param comment Song comment
	 */
	public void setComment(String comment) {
		this.comment = comment.trim();
	}
	/**
	 * Get a byte indicated the genre of the song
	 * @return A byte indicated the genre of the song
	 */
	public byte getGenre() {
		return genre;
	}
	/**
	 * Get a string that indicates the genre of the song
	 * @return Genre string
	 */
	public String getGenreString() {
		if ((this.genre < Tag.genres.length) &&
			(this.genre >= 0))
			return Tag.genres[this.genre];
		else
			return "Unknown";
	}
	/**
	 * Set genre
	 * @param genre New genre
	 */
	public void setGenre(byte genre) {
		this.genre = genre;
	}
	/**
	 * Get title string
	 * @return Song name
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Set title
	 * @param title Song's new title
	 */
	public void setTitle(String title) {
		this.title = title.trim();
		//Check for empty string
		if (this.title.equals(""))
		{
			try {
				this.title = this.filePath.substring(filePath.lastIndexOf('/') + 1, filePath.indexOf('.'));
			} catch(Exception ex) { this.title = "Unknown"; ex.printStackTrace();}
		}
	}
	/**
	 * Get song's year
	 * @return Song's year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * Set new year
	 * @param year Song's new year
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * Get a ByteBuffer that represents the tag of the song
	 * @return ByteBuffer that represents this tag
	 */
	public ByteBuffer toByteBuffer()
	{
		return this.tagValue;
	}
}