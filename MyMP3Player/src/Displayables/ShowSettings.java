package Displayables;
import Files_Handler.FileHandler;
import Memory_Management.Settings;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.media.control.VolumeControl;

/**
 * Settings displayable
 * @author
 */
public class ShowSettings extends Form implements ItemCommandListener {
    private Command cmndOK;
    private Command cmndCancel;
    private MainMenu mainMenu;
    private Gauge volume;
    private ChoiceGroup repeat;
    private ChoiceGroup shuffle;
    private StringItem saveChanges;
    private StringItem discardChanges;
    /**
     * Default c'tor
     * @param mainMenu Main menu containing this class
     */
    public ShowSettings(MainMenu mainMenu)
    {
        super("Settings");

        this.mainMenu = mainMenu;

	/* Set commands */
	cmndOK = new Command("Save", Command.OK, 1);
	cmndCancel = new Command("Cancel", Command.CANCEL, 2);

	/* Show volume, repeat and shuffle settings: */

	/* Volume */
	volume = new Gauge("Volume:", true, 100, mainMenu.settings.getVolume());
	this.append(volume);

	/* Repeat */
	repeat = new ChoiceGroup("Repeat:", ChoiceGroup.POPUP);
	repeat.append("None", null);
	repeat.append("All", null);
	repeat.append("Song", null);
	repeat.setSelectedIndex(this.mainMenu.settings.getRepeat(), true);
	this.append(repeat);

	/* Shuffle */
	shuffle = new ChoiceGroup("Shuffle:", ChoiceGroup.POPUP);
	shuffle.append("On", null);
	shuffle.append("Off", null);
	if (!this.mainMenu.settings.getShuffle())
		shuffle.setSelectedIndex(1, true);
	this.append(shuffle);

	/* Save & Cancel */
	saveChanges = new StringItem("", "Save", StringItem.BUTTON);
	saveChanges.setDefaultCommand(cmndOK);
	saveChanges.setItemCommandListener(this);
	this.append(saveChanges);
	discardChanges = new StringItem("", "Cancel", StringItem.BUTTON);
	discardChanges.setDefaultCommand(cmndCancel);
	discardChanges.setItemCommandListener(this);
	this.append(discardChanges);

	/* Show memory status */
	try
	{
		String[] roots = FileHandler.GetRootListing();
		for (int i = 0; i < roots.length; i++)
		{
			String path = "file:///" + roots[i];
			FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
			this.append(new Gauge("Used Space On " + roots[i] + ":", false,
							(int)(fc.totalSize()/1024/1024),
							(int)(fc.usedSize()/1024/1024)));
			fc.close();
		}
	}catch (Exception ex){ex.printStackTrace();}
    }

    /**
     * Command action
     * @param c Command calling this function
     * @param item Item firing the command
     */
    public void commandAction(Command c, Item item) {
		if (c == cmndOK)
		{
			//Save
			this.mainMenu.settings.setVolume(this.volume.getValue());
			this.mainMenu.settings.setRepeat(this.repeat.getSelectedIndex());
			this.mainMenu.settings.setShuffle(this.shuffle.getSelectedIndex() == 0);
			Settings.SaveSettings(this.mainMenu.settings);
			//If song is playing, adjust it's volume to match
			if (this.mainMenu.currentPlayer != null)
				((VolumeControl)this.mainMenu.currentPlayer.getControl("VolumeControl")).setLevel(this.volume.getValue());
			//Return to main menu
			this.mainMenu.getMainMIDlet().getCurrentDisplay().setCurrent(this.mainMenu);
		}
		else if (c == cmndCancel)
		{
			//Cancel and go back
			/* Discard the changes */
			volume.setValue(mainMenu.settings.getVolume());
			repeat.setSelectedIndex(this.mainMenu.settings.getRepeat(), true);
			if (!this.mainMenu.settings.getShuffle())
				shuffle.setSelectedIndex(1, true);
			else
				shuffle.setSelectedIndex(0, true);
			//Return to main menu
			this.mainMenu.getMainMIDlet().getCurrentDisplay().setCurrent(this.mainMenu);
		}
	}
}