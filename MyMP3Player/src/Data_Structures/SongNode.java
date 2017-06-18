package Data_Structures;

/**
 * Song node element
 * @author
 */
public class SongNode {
    private Song value;
    private SongNode nextNode;
    /**
     * Default c'tor
     * @param value Value of node
     * @param nextNode Next node
     */
    public SongNode(Song value, SongNode nextNode)
    {
        this.value = value;
        this.nextNode = nextNode;
    }
    /**
     * Get the value of the node
     * @return Value of this node
     */
    public Song GetValue()
    {
        return value;
    }
    /**
     * Set the value of this node
     * @param value New value
     */
    public void setValue(Song value) {
		this.value = value;
	}
	/**
     * Get the next node
     * @return The next node
	 */
	public SongNode GetNextNode()
    {
        return this.nextNode;
    }
    /**
     * Set the next node
     * @param nextNode Next node
     */
    public void SetNextNode(SongNode nextNode)
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
