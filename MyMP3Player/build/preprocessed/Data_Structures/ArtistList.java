package Data_Structures;

/**
 * Linked list of artists
 * @author 
 */
public class ArtistList {
    private ArtistNode head;
    /**
     * Length of list
     */
    public int length;
    /**
     * Default c'tor
     */
    public ArtistList()
    {
        this.length = 0;
    }
    /**
     * Convert this list to an array of artists
     * @return Array containing all nodes in this list
     */
    public Artist[] ToArray()
    {
        Artist[] arr = new Artist[this.length];
        ArtistNode tmpNode = this.head;
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
    public Artist ValueAt(int index)
    {
        ArtistNode tmpNode = this.head;
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
    public Artist FindByName(String name)
    {
        ArtistNode tmpNode = this.head;
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
    public int IndexOf(Artist a)
    {
	int index = 0;
        ArtistNode tmpNode = this.head;
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
    public void Add(Artist item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new ArtistNode(item, null);
        else
        {
            ArtistNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
                tmpNode = tmpNode.GetNextNode();
            tmpNode.SetNextNode(new ArtistNode(item, null));
        }
        this.length++;
    }
    /**
     * Sorted addition (Add an item to the list while saving it sorted)
     * @param item Item to add
     */
    public void AddWithoutDuplicates(Artist item)
    {
	    if (this.IndexOf(item) == (-1))
		    this.SortedAddition(item);
    }
    /**
     *
     * @param item
     */
    public void SortedAddition(Artist item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new ArtistNode(item, null);
        else
        {
	    String aName = item.getName();
	    /* compareTo: */
	    /* < 0 -> getName < aName */
	    /* > 0 -> getName > aName */
	    if (this.head.GetValue().getName().compareTo(aName) > 0)
	    {
			ArtistNode newNode = new ArtistNode(item, this.head);
			this.head = newNode;
		    /* Finish */
		    return;
	    }
            ArtistNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
	    {
		   if ((tmpNode.GetNextNode().GetValue().getName().compareTo(aName) >= 0 ) &&
			   (tmpNode.GetValue().getName().compareTo(aName) <= 0 ))
		   {
			ArtistNode newNode = new ArtistNode(item, tmpNode.GetNextNode());
			tmpNode.SetNextNode(newNode);
			this.length++;
			/* Finish */
			return;
		   }
                   tmpNode = tmpNode.GetNextNode();
	    }
            tmpNode.SetNextNode(new ArtistNode(item, null));
        }
        this.length++;
    }
    /**
     * Add an array of items to the list
     * @param items Items to add
     */
    public void AddRange(Artist[] items)
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
