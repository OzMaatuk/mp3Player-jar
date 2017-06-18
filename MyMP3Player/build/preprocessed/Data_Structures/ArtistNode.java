package Data_Structures;

/**
 * Artist node element
 * @author
 */
public class ArtistNode {
    private Artist value;
    private ArtistNode nextNode;
    /**
     * Default c'tor
     * @param value Value of node
     * @param nextNode Next node
     */
    public ArtistNode(Artist value, ArtistNode nextNode)
    {
        this.value = value;
        this.nextNode = nextNode;
    }
    /**
     * Get the value of the node
     * @return Value of this node
     */
    public Artist GetValue()
    {
        return value;
    }
    /**
     * Get the next node
     * @return The next node
     */
    public ArtistNode GetNextNode()
    {
        return this.nextNode;
    }
    /**
     * Set the next node
     * @param nextNode Next node
     */
    public void SetNextNode(ArtistNode nextNode)
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
