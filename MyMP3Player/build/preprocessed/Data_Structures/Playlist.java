package Data_Structures;

/**
 * Playlist class
 * @author
 */
public class Playlist {

	/* Private properties */
	private String path;
	private String name;
	private SongList songs;

	/* C'tors */
	/**
	 * Default c'tor
	 * @param name Name of playlist
	 * @param path Path of playlist
	 */
	public Playlist(String name, String path)
	{
		this.name = name;
		this.path = path;
	}
	/**
	 * C'tor with name, songs list and file path
	 * @param name Name of playlist
	 * @param songs Playlist songs list
	 * @param path Playlist file path
	 */
	public Playlist(String name, SongList songs, String path)
	{
		this.name = name;
		this.songs = songs;
		this.path = path;
	}

	/**
	 * Does this playlist's name equals another playlist's name?
	 * @param obj The second playlist to compare to
	 * @return True - if the playlists has the same name, False - otherwise
	 */
	public boolean equals(Playlist obj) {
		return (this.getName().equals(obj.getName()));
	}

	/* Getters & Setters */
	/**
	 * Get playlist name
	 * @return Name of this playlist
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get the songs list of this playlist
	 * @return Playlist songs
	 */
	public SongList getSongs() {
		return songs;
	}
	/**
	 * Set a new name for this playlist
	 * (The name is not auto saved in the playlist file)
	 * @param name New name for the playlist
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Set a new songs list for this playlist
	 * (The songs are not auto saved in the playlist file)
	 * @param songs
	 */
	public void setSongs(SongList songs) {
		this.songs = songs;
	}
	/**
	 * Get the path of this playlist
	 * @return Playlist file's path
	 */
	public String getPath() {
		return path;
	}
}
