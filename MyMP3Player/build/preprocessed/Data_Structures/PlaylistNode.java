package Data_Structures;

/**
 * Playlist node element
 * @author
 */
public class PlaylistNode {
    private Playlist value;
    private PlaylistNode nextNode;
    /**
     * Default c'tor
     * @param value Value of node
     * @param nextNode Next node
     */
    public PlaylistNode(Playlist value, PlaylistNode nextNode)
    {
        this.value = value;
        this.nextNode = nextNode;
    }
    /**
     * Get the value of the node
     * @return Value of this node
     */
    public Playlist GetValue()
    {
        return value;
    }
    /**
     * Get the next node
     * @return The next node
     */
    public PlaylistNode GetNextNode()
    {
        return this.nextNode;
    }
    /**
     * Set the next node
     * @param nextNode Next node
     */
    public void SetNextNode(PlaylistNode nextNode)
    {
        this.nextNode = nextNode;
    }
}
