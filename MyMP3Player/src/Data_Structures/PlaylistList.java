package Data_Structures;

/**
 * Linked list of playlists
 * @author
 */
public class PlaylistList {
    private PlaylistNode head;
    /**
     * Length of list
     */
    public int length;
    /**
     * Default c'tor
     */
    public PlaylistList()
    {
        this.length = 0;
    }
    /**
     * Convert this list to an array of playlists
     * @return Array containing all nodes in this list
     */
    public Playlist[] ToArray()
    {
        Playlist[] arr = new Playlist[this.length];
        PlaylistNode tmpNode = this.head;
        int i = 0;
        while (tmpNode != null)
        {
            arr[i++] = tmpNode.GetValue();
            tmpNode = tmpNode.GetNextNode();
        }
        return arr;
    }
    /**
     * Get the value at the given index
     * @param index Index of the element to get
     * @return Element at given index
     */
    public Playlist ValueAt(int index)
    {
        PlaylistNode tmpNode = this.head;
        for (; index > 0; index--)
        {
            tmpNode = tmpNode.GetNextNode();
        }
        return tmpNode.GetValue();
    }
    /**
     *
     * @param name
     * @return
     */
    public Playlist FindByName(String name)
    {
        PlaylistNode tmpNode = this.head;
        while (tmpNode != null)
        {
		if (tmpNode.GetValue().getName().equals(name))
			return tmpNode.GetValue();
		tmpNode = tmpNode.GetNextNode();
        }
	//Not found
	return null;
    }
    /**
     * Get the index of the given item
     * @param a Item to search
     * @return Index of item ((-1) if item is not found)
     */
    public int IndexOf(Playlist a)
    {
	int index = 0;
        PlaylistNode tmpNode = this.head;
        while (tmpNode != null)
        {
		if (tmpNode.GetValue().equals(a))
			return index;
		index++;
		tmpNode = tmpNode.GetNextNode();
        }
	//Not found
	return (-1);
    }
    /**
     * Add a new item to the end of this list
     * @param item Item to add
     */
    public void Add(Playlist item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new PlaylistNode(item, null);
        else
        {
            PlaylistNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
                tmpNode = tmpNode.GetNextNode();
            tmpNode.SetNextNode(new PlaylistNode(item, null));
        }
        this.length++;
    }
    /**
     * Sorting addition to the list avoiding duplicate items
     * @param item Item to add
     */
    public void AddWithoutDuplicates(Playlist item)
    {
	    if (this.IndexOf(item) == (-1))
		    this.SortedAddition(item);
    }
    /**
     * Sorted addition (Add an item to the list while saving it sorted)
     * @param item Item to add
     */
    public void SortedAddition(Playlist item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new PlaylistNode(item, null);
        else
        {
            PlaylistNode tmpNode = this.head;
	    String aName = item.getName();
            while (tmpNode.GetNextNode() != null)
	    {
		   if ((tmpNode.GetNextNode().GetValue().getName().compareTo(aName) > 0 ) &&
			   (tmpNode.GetValue().getName().compareTo(aName) <= 0 ))
		   {
			PlaylistNode newNode = new PlaylistNode(item, tmpNode.GetNextNode());
			tmpNode.SetNextNode(newNode);
			this.length++;
		   }
                   tmpNode = tmpNode.GetNextNode();
	    }
            tmpNode.SetNextNode(new PlaylistNode(item, null));
        }
        this.length++;
    }
    /**
     * Add an array of items to the list
     * @param items Items to add
     */
    public void AddRange(Playlist[] items)
    {
	    for (int i = 0; i< items.length; i++)
		    this.Add(items[i]);
    }
    /**
     * Delete all items from the list
     */
    public void DeleteAll()
    {
        this.length = 0;
        this.head = null;
    }
    /**
     * Remove item by a given index
     * @param index The index of the item to remove
     */
    public void RemoveAt(int index) {

		//Check input
		if (index >= this.length)
			throw new ArrayIndexOutOfBoundsException();

		int i = index;
		PlaylistNode tmpNode = this.head;
		PlaylistNode prevNode = null;

		//Change head
		if (index == 0)
			this.head = this.head.GetNextNode();
		else
		{
			for (; i > 0; i--)
			{
				prevNode = tmpNode;
				tmpNode = tmpNode.GetNextNode();
			}
			prevNode.SetNextNode(tmpNode.GetNextNode());
		}
	}
}
