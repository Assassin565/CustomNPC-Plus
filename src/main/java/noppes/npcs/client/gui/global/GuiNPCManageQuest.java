package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiMailmanSendSetup;
import noppes.npcs.client.gui.SubGuiNpcCommand;
import noppes.npcs.client.gui.SubGuiNpcFactionOptions;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeDialog;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeKill;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeLocation;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.constants.*;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;
import java.util.Vector;

public class GuiNPCManageQuest extends GuiNPCInterface2 implements IScrollData, ISubGuiListener, GuiSelectionListener,ICustomScrollListener,ITextfieldListener, IGuiData
{
	private GuiCustomScroll scroll;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	public static Quest quest = new Quest();
	private QuestCategory category = new QuestCategory();
	private boolean categorySelection = true;
	private boolean questlogTA = false;
	
	public static GuiScreen Instance;
	
    public GuiNPCManageQuest(EntityNPCInterface npc)
    {
    	super(npc);
    	Instance = this;
		Client.sendData(EnumPacketServer.QuestCategoriesGet);
    }

    public void initGui()
    {
        super.initGui();
        Minecraft.getMinecraft().displayGuiScreen(new BridgeManage(this.npc));

    }

	private void dialogGuiInit() {}

	private void categoryGuiInit() {}

	public void buttonEvent(GuiButton guibutton){}

	@Override
	public void setGuiData(NBTTagCompound compound) {}

	@Override
	public void subGuiClosed(SubGuiInterface subgui){}
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {}
	
	@Override
	public void selected(int id, String name) {}
	
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {}
	
	@Override
	public void close(){}

	@Override
	public void save() {}

	@Override
	public void setSelected(String selected) {}

}
