package noppes.npcs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import foxz.command.CommandNoppes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import nikedemos.markovnames.generators.*;
import noppes.npcs.config.ConfigLoader;
import noppes.npcs.config.ConfigProp;
import noppes.npcs.controllers.*;
import noppes.npcs.enchants.EnchantInterface;
import noppes.npcs.entity.*;
import noppes.npcs.entity.old.*;
import noppes.npcs.scripted.NpcAPI;

import java.io.File;
import java.util.Set;

@Mod(modid = "customnpcs", name = "CustomNpcs", version = "1.6-beta2")
public class CustomNpcs {

    @ConfigProp(info = "Disable Chat Bubbles")
    public static boolean EnableChatBubbles = true;

    private static int NewEntityStartId = 0;

    @ConfigProp(info = "Enables/Disables ALL scripting. You can still see and write code in the scripter, but these scripts won't run. True by default")
    public static boolean ScriptingEnabled = true;

    @ConfigProp(info = "Enables global player event scripts to be used in the scripter. You can still see and write code in the scripter, but these scripts won't run. True by default.")
    public static boolean GlobalPlayerScripts = true;

    @ConfigProp(info = "Enables global forge event scripts to be used in the scripter. You can still see and write code in the scripter, but these scripts won't run. True by default.")
    public static boolean GlobalForgeScripts = true;

    @ConfigProp(info = "Enables global NPC scripts to be used in the scripter. You can still see and write code in the scripter, but these scripts won't run. False by default, use with caution!")
    public static boolean GlobalNPCScripts = false;

    @ConfigProp(info = "Navigation search range for NPCs. Not recommended to increase if you have a slow pc or on a server. Minimum of 16, maximum of 96.")
    public static int NpcNavRange = 32;

    @ConfigProp(info = "Size limit for NPCs. Default 100, larger sizes may cause lag on clients and servers that can't take it!")
    public static int NpcSizeLimit = 100;

    @ConfigProp(info = "Set to true if you want the dialog command option to be able to use op commands like tp etc")
    public static boolean NpcUseOpCommands = false;

    @ConfigProp(info = "Client sided! Determines where tracking quest info shows up on the screen based on a number from 0 to 8. Default: 3")
    public static int TrackingInfoAlignment = 3;

    @ConfigProp(info = "Client sided! Offsets the tracking info GUI by this amount in the X direction.")
    public static int TrackingInfoX = 0;

    @ConfigProp(info = "Client sided! Offsets the tracking info GUI by this amount in the Y direction.")
    public static int TrackingInfoY = 0;

    @ConfigProp
    public static boolean InventoryGuiEnabled = true;

    @ConfigProp
    public static boolean DisableExtraItems = false;
    @ConfigProp
    public static boolean DisableExtraBlock = false;

    public static long ticks;

    @SidedProxy(clientSide = "noppes.npcs.client.ClientProxy", serverSide = "noppes.npcs.CommonProxy")
    public static CommonProxy proxy;

    @ConfigProp(info = "Enables CustomNpcs startup update message")
    public static boolean EnableUpdateChecker = true;

    public static CustomNpcs instance;

    public static boolean FreezeNPCs = false;

    @ConfigProp(info = "Only ops can create and edit npcs")
    public static boolean OpsOnly = false;
    
    @ConfigProp(info = "Default interact line. Leave empty to not have one")
    public static String DefaultInteractLine = "Hello @p";

    @ConfigProp
    public static boolean DisableEnchants = false;
    @ConfigProp(info = "Start Id for enchants. IDs can only range from 0-256")
    public static int EnchantStartId = 100;

    @ConfigProp(info = "Number of chunk loading npcs that can be active at the same time")
    public static int ChuckLoaders = 20;

    @ConfigProp(info = "The maximum number of images any dialog can hold.")
    public static int DialogImageLimit = 10;

    @ConfigProp(info = "The maximum number of images any dialog can hold.")
    public static int SkinOverlayLimit = 10;

    public static File Dir;

    @ConfigProp(info = "Set to false if you want to disable guns")
    public static boolean GunsEnabled = true;

    @ConfigProp(info = "Enables leaves decay")
    public static boolean LeavesDecayEnabled = true;
    
    @ConfigProp(info = "Enables Vine Growth")
    public static boolean VineGrowthEnabled = true;

    @ConfigProp(info = "Enables Ice Melting")
    public static boolean IceMeltsEnabled = true;
    
    @ConfigProp(info = "Normal players can use soulstone on animals")
	public static boolean SoulStoneAnimals = true;
    
    @ConfigProp(info = "Normal players can use soulstone on villagers")
	public static boolean SoulStoneVillagers = false;
    
    @ConfigProp(info = "Normal players can use soulstone on all npcs")
	public static boolean SoulStoneNPCs = false;
    
    @ConfigProp(info = "Normal players can use soulstone on friendly npcs")
	public static boolean SoulStoneFriendlyNPCs = false;

	@ConfigProp(info="When set to Minecraft it will use minecrafts font, when Default it will use OpenSans. Can only use fonts installed on your PC")
	public static String FontType = "Default";

	@ConfigProp(info="Font size for custom fonts (doesn't work with minecrafts font)")
	public static int FontSize = 18;

    public static FMLEventChannel Channel;
    public static FMLEventChannel ChannelPlayer;
    
    public static ConfigLoader Config;

    public static final MarkovGenerator[] MARKOV_GENERATOR = new MarkovGenerator[10];

    public CustomNpcs() {
        instance = this;
    }

    @EventHandler
    public void load(FMLPreInitializationEvent ev) {
        Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCs");
        ChannelPlayer = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCsPlayer");

        MinecraftServer server = MinecraftServer.getServer();
        String dir = "";
        if (server != null) {
            dir = new File(".").getAbsolutePath();
        } else {
            dir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
        }
        Dir = new File(dir, "customnpcs");
        Dir.mkdir();

        Config = new ConfigLoader(this.getClass(), new File(dir, "config"), "CustomNpcs");
        Config.loadConfig();

        if (NpcNavRange < 16) {
            NpcNavRange = 16;
        }
        if (NpcNavRange > 96) {
            NpcNavRange = 96;
        }

        if(NpcSizeLimit < 1)
            NpcSizeLimit = 1;

        if (DialogImageLimit < 0) {
            DialogImageLimit = 0;
        }

        if (SkinOverlayLimit < 0) {
            SkinOverlayLimit = 0;
        }

        if (TrackingInfoAlignment < 0)
            TrackingInfoAlignment = 0;
        if (TrackingInfoAlignment > 8)
            TrackingInfoAlignment = 8;

        EnchantInterface.load();
        CustomItems.load();
        
        proxy.load();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        MinecraftForge.EVENT_BUS.register(new ServerEventsHandler());
        MinecraftForge.EVENT_BUS.register(new ScriptController());

        ScriptPlayerEventHandler scriptPlayerEventHandler = new ScriptPlayerEventHandler();
        MinecraftForge.EVENT_BUS.register(scriptPlayerEventHandler);
        FMLCommonHandler.instance().bus().register(scriptPlayerEventHandler);

        ScriptForgeEventHandler forgeEventHandler = (new ScriptForgeEventHandler()).registerForgeEvents();
        MinecraftForge.EVENT_BUS.register(forgeEventHandler);
        FMLCommonHandler.instance().bus().register(forgeEventHandler);

        ScriptItemEventHandler scriptItemEventHandler = new ScriptItemEventHandler();
        MinecraftForge.EVENT_BUS.register(scriptItemEventHandler);
        FMLCommonHandler.instance().bus().register(scriptItemEventHandler);

		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
        
        registerNpc(EntityNPCHumanMale.class, "npchumanmale");
        registerNpc(EntityNPCVillager.class, "npcvillager");
        registerNpc(EntityNpcPony.class, "npcpony");
        registerNpc(EntityNPCHumanFemale.class, "npchumanfemale");
        registerNpc(EntityNPCDwarfMale.class, "npcdwarfmale");
        registerNpc(EntityNPCFurryMale.class, "npcfurrymale");
        registerNpc(EntityNpcMonsterMale.class, "npczombiemale");
        registerNpc(EntityNpcMonsterFemale.class, "npczombiefemale");
        registerNpc(EntityNpcSkeleton.class, "npcskeleton");
        registerNpc(EntityNPCDwarfFemale.class, "npcdwarffemale");
        registerNpc(EntityNPCFurryFemale.class, "npcfurryfemale");
        registerNpc(EntityNPCOrcMale.class, "npcorcfmale");
        registerNpc(EntityNPCOrcFemale.class, "npcorcfemale");
        registerNpc(EntityNPCElfMale.class, "npcelfmale");
        registerNpc(EntityNPCElfFemale.class, "npcelffemale");
        registerNpc(EntityNpcCrystal.class, "npccrystal");
        registerNpc(EntityNpcEnderchibi.class, "npcenderchibi");
        registerNpc(EntityNpcNagaMale.class, "npcnagamale");
        registerNpc(EntityNpcNagaFemale.class, "npcnagafemale");
        registerNpc(EntityNpcSlime.class, "NpcSlime");
        registerNpc(EntityNpcDragon.class, "NpcDragon");
        registerNpc(EntityNPCEnderman.class, "npcEnderman");
        registerNpc(EntityNPCGolem.class, "npcGolem");
        registerNpc(EntityCustomNpc.class, "CustomNpc");

        registerNewEntity(EntityChairMount.class, "CustomNpcChairMount", 64, 10, false);
        registerNewEntity(EntityProjectile.class, "throwableitem", 64, 3, true);
        registerNewEntity(EntityMagicProjectile.class, "magicprojectile", 64, 3, true);

        new RecipeController();

        ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkController());

        new CustomNpcsPermissions();
        
        PixelmonHelper.load();
    }

    @EventHandler
    public void load(FMLInitializationEvent ev) {

        MARKOV_GENERATOR[0] = new MarkovRoman(3);
        MARKOV_GENERATOR[1] = new MarkovJapanese(4);
        MARKOV_GENERATOR[2] = new MarkovSlavic(3);
        MARKOV_GENERATOR[3] = new MarkovWelsh(3);
        MARKOV_GENERATOR[4] = new MarkovSaami(3);
        MARKOV_GENERATOR[5] = new MarkovOldNorse(4);
        MARKOV_GENERATOR[6] = new MarkovAncientGreek(3);
        MARKOV_GENERATOR[7] = new MarkovAztec(3);
        MARKOV_GENERATOR[8] = new MarkovCustomNPCsClassic(3);
        MARKOV_GENERATOR[9] = new MarkovSpanish(3);

    }

    @EventHandler
    public void setAboutToStart(FMLServerAboutToStartEvent event) {
    	ChunkController.instance.clear();
        new QuestController();
        new PlayerDataController();
        new FactionController();
        new TransportController();
        new GlobalDataController();
        new SpawnController();
        new LinkedNpcController();
        ScriptController.Instance.loadStoredData();
        ScriptController.Instance.loadForgeScripts();
        ScriptController.Instance.loadNPCScripts();
        ScriptController.Instance.loadPlayerScripts();
        ScriptController.HasStart = false;
        NpcAPI.clearCache();

        Set<String> names = Block.blockRegistry.getKeys();
        for(String name : names){
        	Block block = (Block) Block.blockRegistry.getObject(name);
        	if(block instanceof BlockLeavesBase){
        		block.setTickRandomly(LeavesDecayEnabled);
        	}
        	if(block instanceof BlockVine){
        		block.setTickRandomly(VineGrowthEnabled);
        	}
        	if(block instanceof BlockIce){
        		block.setTickRandomly(IceMeltsEnabled);
        	}
        }
    }
    
    //Loading items in the about to start event was corrupting items with a damage value
    @EventHandler
    public void started(FMLServerStartedEvent event) {
    	RecipeController.instance.load();
        new DialogController();
        new BankController();
        QuestController.instance.load();
        ScriptController.HasStart = true;
        ServerCloneController.Instance = new ServerCloneController();
    }
    
    @EventHandler
    public void stopped(FMLServerStoppedEvent event){
        ServerCloneController.Instance = null;
        GlobalDataController.instance.saveData();
    }

    @EventHandler
    public void serverstart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandNoppes());
    }

    private void registerNpc(Class<? extends Entity> cl, String name) {
        EntityRegistry.registerModEntity(cl, name, NewEntityStartId++, this, 64, 3, true);
        EntityList.stringToClassMapping.put(name, cl);
    }

    private void registerNewEntity(Class<? extends Entity> cl, String name, int range, int update, boolean velocity) {
        EntityRegistry.registerModEntity(cl, name, NewEntityStartId++, this, range, update, velocity);
    }


    public static File getWorldSaveDirectory() {
        MinecraftServer server = MinecraftServer.getServer();
        File saves = new File(".");
        if (server != null && !server.isDedicatedServer()) {
            saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
        }
        if (server != null) {
            File savedir = new File(new File(saves, server.getFolderName()), "customnpcs");
            if (!savedir.exists()) {
                savedir.mkdir();
            }
            return savedir;
        }
        return null;
    }

    public static File getWorldSaveDirectory(String s) {
        try {
            File dir = new File(".");
            if (getServer() != null) {
                if (!getServer().isDedicatedServer()) {
                    dir = new File(Minecraft.getMinecraft().mcDataDir, "saves");
                }

                dir = new File(new File(dir, getServer().getFolderName()), "customnpcs");
            }

            if (s != null) {
                dir = new File(dir, s);
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }

            return dir;
        } catch (Exception var2) {
            LogWriter.error("Error getting worldsave", var2);
            return null;
        }
    }

    public static MinecraftServer getServer(){
        return MinecraftServer.getServer();
    }
}
