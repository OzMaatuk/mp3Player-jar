package Data_Structures;

/**
 * Linked list of albums
 * @author
 */
public class AlbumList {
    private AlbumNode head;
    /**
     * Length of list
     */
    public int length;
    /**
     * Default c'tor
     */
    public AlbumList()
    {
        this.length = 0;
    }
    /**
     * Convert this list to an array of songs
     * @return Array containing all nodes in this list
     */
    public Album[] ToArray()
    {
        Album[] arr = new Album[this.length];
        AlbumNode tmpNode = this.head;
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
    public Album ValueAt(int index)
    {
        AlbumNode tmpNode = this.head;
        for (; index > 0; index--)
        {
            tmpNode = tmpNode.GetNextNode();
        }
        return tmpNode.GetValue();
    }

    /**
     * Find album by it's name
     * @param name Name to search for
     * @return First album matching the given name
     */
    public Album FindByName(String name)
    {
        AlbumNode tmpNode = this.head;
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
    public int IndexOf(Album a)
    {
	int index = 0;
        AlbumNode tmpNode = this.head;
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
     * Sorting addition to the list avoiding duplicate items
     * @param item Item to add
     */
    public void AddWithoutDuplicates(Album item)
    {
	    if (this.IndexOf(item) == (-1))
		    this.SortedAddition(item);
    }
    /**
     * Sorted addition (Add an item to the list while saving it sorted)
     * @param item Item to add
     */
    public void SortedAddition(Album item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new AlbumNode(item, null);
        else
        {
	    String aName = item.getName();
	    /* compareTo: */
	    /* < 0 -> getName < aName */
	    /* > 0 -> getName > aName */
	    if (this.head.GetValue().getName().compareTo(aName) > 0)
	    {
			AlbumNode newNode = new AlbumNode(item, this.head);
			this.head = newNode;
		    /* Finish */
		    return;
	    }
            AlbumNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
	    {
		   if ((tmpNode.GetNextNode().GetValue().getName().compareTo(aName) >= 0 ) &&
			   (tmpNode.GetValue().getName().compareTo(aName) <= 0 ))
		   {
			AlbumNode newNode = new AlbumNode(item, tmpNode.GetNextNode());
			tmpNode.SetNextNode(newNode);
			this.length++;
			return;
		   }
                   tmpNode = tmpNode.GetNextNode();
	    }
            tmpNode.SetNextNode(new AlbumNode(item, null));
        }
        this.length++;
    }

    /**
     * Add a new item to the end of this list
     * @param item Item to add
     */
    public void Add(Album item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new AlbumNode(item, null);
        else
        {
            AlbumNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
                tmpNode = tmpNode.GetNextNode();
            tmpNode.SetNextNode(new AlbumNode(item, null));
        }
        this.length++;
    }
    /**
     * Add an array of items to the list
     * @param items Items to add
     */
    public void AddRange(Album[] items)
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
}
