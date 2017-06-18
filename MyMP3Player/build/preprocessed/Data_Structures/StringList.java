package Data_Structures;

/**
 * Linked list of strings
 * @author
 */
public class StringList {
    private StringNode head;
    /**
     * Length of list
     */
    public int length;
    /**
     * Default c'tor
     */
    public StringList()
    {
        this.length = 0;
    }
    /**
     * Convert this list to an array of songs
     * @return Array containing all nodes in this list
     */
    public String[] ToArray()
    {
        String[] arr = new String[this.length];
        StringNode tmpNode = this.head;
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
    public String ValueAt(int index)
    {
        StringNode tmpNode = this.head;
        for (; index > 0; index--)
        {
            tmpNode = tmpNode.GetNextNode();
        }
        return tmpNode.GetValue();
    }
    /**
     * Add a new item to the end of this list
     * @param item Item to add
     */
    public void Add(String item)
    {
        //Add a new item to the list
        if (this.head == null)
            this.head = new StringNode(item, null);
        else
        {
            StringNode tmpNode = this.head;
            while (tmpNode.GetNextNode() != null)
                tmpNode = tmpNode.GetNextNode();
            tmpNode.SetNextNode(new StringNode(item, null));
        }
        this.length++;
    }
    /**
     * Add an array of items to the list
     * @param items Items to add
     */
    public void AddRange(String[] items)
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
