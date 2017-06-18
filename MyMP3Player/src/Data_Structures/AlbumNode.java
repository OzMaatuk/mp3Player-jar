package Data_Structures;

/**
 * Album node element
 * @author
 */
public class AlbumNode {
    private Album value;
    private AlbumNode nextNode;
    /**
     * Default c'tor
     * @param value Value of node
     * @param nextNode Next node
     */
    public AlbumNode(Album value, AlbumNode nextNode)
    {
        this.value = value;
        this.nextNode = nextNode;
    }
    /**
     * Get the value of the node
     * @return Value of this node
     */
    public Album GetValue()
    {
        return value;
    }
    /**
     * Get the next node
     * @return The next node
     */
    public AlbumNode GetNextNode()
    {
        return this.nextNode;
    }
    /**
     * Set the next node
     * @param nextNode Next node
     */
    public void SetNextNode(AlbumNode nextNode)
    {
        this.nextNode = nextNode;
    }
    
    /**
     * ToString override
     * @return toString of the value of this node and if it's null then "Null"
     */
    public String toString() {
		if (this.value != null)
			return this.value.toString();
		else
			return "Null";
	}
}
