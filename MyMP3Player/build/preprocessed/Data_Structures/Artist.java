package Data_Structures;

/**
 * Artist class
 * @author
 */
public class Artist {
	/*Private Properties*/
	private String name;
	private AlbumList artistAlbums;

	/**
	 * Default c'tor
	 * @param name Name of artist
	 */
	public Artist(String name) {
		this.name = name;
		this.artistAlbums = new AlbumList();
	}
	/**
	 * C'tor with both name & list of albums
	 * @param name Artist name
	 * @param albums Artist albums
	 */
	public Artist(String name, AlbumList albums) {
		this.name = name;
		this.artistAlbums = albums;
	}
	/**
	 * Does this artist equals another one?
	 * @param a The artist to compare to
	 * @return True - if they're equal, and false otherwise
	 */
	public boolean equals(Artist a)
	{
		return this.name.equals(a.name);
	}

	/* Getters & Setters */
	/**
	 * Get artist name
	 * @return Artist name
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * Set artist name
	 * @param name Artist name
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 * Get artist initials
	 * @return Artist initials seperated by a dot.
	 */
	public String getInitials() {
		String initials = "";
		char[] charArr = this.name.toUpperCase().toCharArray();
		initials += charArr[0] + ".";
		for (int i = 2; i < charArr.length; i++)
			if ((charArr[i - 1] == ' ') ||
				(charArr[i - 1] == '/') ||
				(charArr[i - 1] == '-') ||
				(charArr[i - 1] == '_') ||
				(charArr[i - 1] == '.'))
					initials += charArr[i] + ".";
		return initials;
	}

	/**
	 * Get artist albums
	 * @return Artist albums
	 */
	public AlbumList getAlbums(){
		return this.artistAlbums;
	}
	/**
	 * Set artist albums
	 * @param albums New list of artist albums
	 */
	public void setAlbums(AlbumList albums){
		this.artistAlbums = albums;
	}
	/**
	 * ToString override
	 * @return Name of artist
	 */
	public String toString() {
		return this.name;
	}
}
