package Data_Structures;

/**
 * Album class
 * @author
 */
public class Album {
	private String name;
	private SongList albumSongs;
	private Artist artist;
	/**
	 * Default c'tor
	 * @param name Album name
	 * @param a Album artist
	 * @param songs Album songs
	 */
	public Album(String name, Artist a, SongList songs)
	{
		this.name = name;
		this.artist = a;
		this.albumSongs = songs;
	}
	/**
	 * Does this album equals another one? (Same name & artist)
	 * @param a The album to compare to
	 * @return True - if they're equal, False otherwise
	 */
	public boolean equals(Album a)
	{
		return ((this.name.equals(a.getName())) && (this.artist.equals(a.getArtist())));
	}

	//Getters & Setters
	/**
	 * Get album name
	 * @return Album name
	 */
	public String getName()
	{
		return this.name;
	}
	/**
	 * Get album artist
	 * @return Album artist
	 */
	public Artist getArtist()
	{
		return this.artist;
	}
	/**
	 * Get album songs
	 * @return Album songs
	 */
	public SongList getSongs()
	{
		return this.albumSongs;
	}

	/**
	 * Set album name
	 * @param newName New album name
	 */
	public void setName(String newName)
	{
		this.name = newName;
	}
	/**
	 * Set album artist
	 * @param newAritst New album artist
	 */
	public void setArtist(Artist newAritst)
	{
		this.artist = newAritst;
	}
	/**
	 * Set album songs list
	 * @param newSongs New album songs list
	 */
	public void setSongs(SongList newSongs)
	{
		this.albumSongs = newSongs;
	}
	/**
	 * ToString override
	 * @return Name of album
	 */
	public String toString() {
		return this.name;
	}
}
