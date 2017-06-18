package Displayables;
import Data_Structures.SongList;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * Play all songs
 * @author
 */
public class PlayAll extends List implements CommandListener {
    private Command cmndBack;
    private MainMenu mainMenu;
    private Mp3Player mp3Player;
    private SongList songs;
    /**
     * Default c'tor
     * @param mainMenu Main menu containing this class
     */
    public PlayAll(MainMenu mainMenu)
    {
        super("Play All", List.IMPLICIT);

        this.mainMenu = mainMenu;

	this.songs = mainMenu.songs;

	for (int i = 0; i < this.songs.length; i++)
			this.append(this.songs.ValueAt(i).getTag().getTitle(), null);
		
        cmndBack = new Command("Back", Command.CANCEL, 1);
        this.addCommand(cmndBack);
        this.setCommandListener(this);
    }

    /**
     * Command action
     * @param c Command calling this function
     * @param d Currently displayed displayable
     */
    public void commandAction(Command c, Displayable d) {
        if (c == cmndBack)
        {
            this.mainMenu.getMainMIDlet().returnToMainMenu();
        } else if (c == List.SELECT_COMMAND)
	{
		mp3Player = new Mp3Player(this.mainMenu, this, this.songs, this.getSelectedIndex());
		this.mainMenu.getMainMIDlet().getCurrentDisplay().setCurrent(mp3Player);
	}
    }
}