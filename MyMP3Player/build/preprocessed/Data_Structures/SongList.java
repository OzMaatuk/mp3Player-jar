package Data_Structures;

import java.util.Random;

/**
 * Linked list of songs
 * @author
 */
public class SongList {
    private SongNode head;
    /**
     * Length of list
     */
    public int length;
    /**
     * Default c'tor
     */
    public SongList()
    {
	    this.head = null;
	    this.length = 0;
    }
    /**
     * Convert this list to an array of songs
     * @return Array containing all nodes in this list
     */
    public Song[] ToArray()
    {
        Song[] arr = new Song[this.length];
        SongNode tmpNode = this.head;
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
    public Song ValueAt(int index)
    {
        SongNode tmpNode = this.head;
        for (; index > 0; index--)
        {
            tmpNode = tmpNode.GetNextNode();
        }
        return tmpNode.GetValue();
    }

    /**
     * Get the index of the given item
     * @param a Item to search
     * @return Index of item ((-1) if item is not found)
     */
    public int IndexOf(Song a)
    {
	int index = 0;
        SongNode tmpNode = this.head;
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
    public void AddWithoutDuplicates(Song item)
    {
	    if (this.IndexOf(item) == (-1))
		    this.SortedAddition(item);
    }
    /**
     * Sorted addition (Add an item to the list while saving it sorted)
     * @param item Item to add
     */
    public void SortedAddition(Song item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new SongNode(item, null);
        else
        {try{
	    String sTitle = item.getTag().getTitle();
	    /* compareTo: */
	    /* < 0 -> getName < aName */
	    /* > 0 -> getName > aName */
	    if (this.head.GetValue().getTag().getTitle().compareTo(sTitle) > 0)
	    {
			SongNode newNode = new SongNode(item, this.head);
			this.head = newNode;
		    /* Finish */
		    return;
	    }
            SongNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
	    {
		   if ((tmpNode.GetNextNode().GetValue().getTag().getTitle().compareTo(sTitle) >= 0 ) &&
			   (tmpNode.GetValue().getTag().getTitle().compareTo(sTitle) <= 0 ))
		   {
			SongNode newNode = new SongNode(item, tmpNode.GetNextNode());
			tmpNode.SetNextNode(newNode);
			this.length++;
			/* Finish */
			return;
		   }
                   tmpNode = tmpNode.GetNextNode();
	    }
            tmpNode.SetNextNode(new SongNode(item, null));}catch(Exception ex){ex.printStackTrace();}
        }
        this.length++;
    }

    /**
     * Add a new item to the end of this list
     * @param item Item to add
     */
    public void Add(Song item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new SongNode(item, null);
        else
        {
            SongNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
                tmpNode = tmpNode.GetNextNode();
            tmpNode.SetNextNode(new SongNode(item, null));
        }
        this.length++;
    }
    /**
     * Add an array of items to the list
     * @param items Items to add
     */
    public void AddRange(Song[] items)
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
     * Recursion for shuffling the list
     * @param head Head of the lsit to shuffle
     * @param len Length of the list to shuffle
     */
	private void shuffleRec(SongNode head, int len)
	{
		//if len<2 then there's nothing to shuffle
		if(len<2)
			return;

		//Set second list's head
		SongNode head2 = head;

		//Splitting list,  get first element of second list tp head2
		for (int i = 0; i < len/2; i++)
			head2 = head2.GetNextNode();
		
		//Shuffle first list
		this.shuffleRec(head, len/2);
		//Shuffle second list
		this.shuffleRec(head2, len-len/2);

		Random rnd = new Random();
		//Merge 2 lists
		for(int i = 0; i < Math.min(len/2, len - len/2); i++){
			  if(rnd.nextInt()%2 == 0) {
					//Swap values of two elements
					Song data = head.GetValue();
					head.setValue(head2.GetValue());
					head2.setValue(data);
			  }
			  //Go to the next elements
			  head = head.GetNextNode();
			  head2 = head2.GetNextNode();
		}
	}

	/**
	 * Shuffle this list
	 */
	public void Shuffle() {
		//Shuffle this list
		this.shuffleRec(head, length);
	}

	/**
	 * Create a clone of this list
	 * @return A clone of this list
	 */
	public SongList clone() {
		//Make a clone of this list
		SongList clone = new SongList();
		SongNode tmpNode = this.head;

		while (tmpNode != null)
		{
			clone.Add(tmpNode.GetValue());
			tmpNode = tmpNode.GetNextNode();
		}
		
		return clone;
	}
	
	/**
	 * Sort this list
	 */
	public void Sort() {
		//Make a clone of this list
		SongList clone = this.clone();
		this.DeleteAll();

		SongNode tmpNode = clone.head;

		while (tmpNode != null)
		{
			this.SortedAddition(tmpNode.GetValue());
			tmpNode = tmpNode.GetNextNode();
		}
	}

	/**
	 * Get the head of the list
	 * @return The head of this list
	 */
	public SongNode getHead()
	{
	    return this.head;
	}
}
