package Data_Structures;

/**
 * String node element
 * @author
 */
public class StringNode {
    private String value;
    private StringNode nextNode;
    /**
     * Default c'tor
     * @param value Value of node
     * @param nextNode Next node
     */
    public StringNode(String value, StringNode nextNode)
    {
        this.value = value;
        this.nextNode = nextNode;
    }
    /**
     * Get the value of the node
     * @return Value of this node
     */
    public String GetValue()
    {
        return value;
    }
    /**
     * Get the next node
     * @return The next node
     */
    public StringNode GetNextNode()
    {
        return this.nextNode;
    }
    /**
     * Set the next node
     * @param nextNode Next node
     */
    public void SetNextNode(StringNode nextNode)
    {
        this.nextNode = nextNode;
    }
}
