package noppes.npcs.scripted.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.entity.IPlayer;

public class CustomGuiEvent extends Event {
    public final IPlayer player;
    public final ICustomGui gui;

    public CustomGuiEvent(IPlayer player, ICustomGui gui) {
        this.player = player;
        this.gui = gui;
    }

    public static class ButtonEvent extends CustomGuiEvent {
        public final int buttonId;

        public ButtonEvent(IPlayer player, ICustomGui gui, int buttonId) {
            super(player, gui);
            this.buttonId = buttonId;
        }
    }

    public static class UnfocusedEvent extends CustomGuiEvent {
        public final int textfieldId;

        public UnfocusedEvent(IPlayer player, ICustomGui gui, int textfieldId) {
            super(player, gui);
            this.textfieldId = textfieldId;
        }
    }

    public static class CloseEvent extends CustomGuiEvent {
        public CloseEvent(IPlayer player, ICustomGui gui) {
            super(player, gui);
        }
    }

    public static class ScrollEvent extends CustomGuiEvent {
        public final int scrollId;
        public final String[] selection;
        public final boolean doubleClick;
        public final int scrollIndex;

        public ScrollEvent(IPlayer player, ICustomGui gui, int scrollId, int scrollIndex, String[] selection, boolean doubleClick) {
            super(player, gui);
            this.scrollId = scrollId;
            this.selection = selection;
            this.doubleClick = doubleClick;
            this.scrollIndex = scrollIndex;
        }
    }

    public static class SlotEvent extends CustomGuiEvent {
        public final int slotId;
        public final IItemStack stack;

        public SlotEvent(IPlayer player, ICustomGui gui, int slotId, IItemStack stack) {
            super(player, gui);
            this.slotId = slotId;
            this.stack = stack;
        }
    }

    @Cancelable
    public static class SlotClickEvent extends CustomGuiEvent {
        public final int slotId;
        public final IItemStack stack;
        public final int dragType;
        /**
         *
         */
        public final int clickType;

        public SlotClickEvent(IPlayer player, ICustomGui gui, int slotId, IItemStack stack, int dragType, int clickType) {
            super(player,gui);
            this.slotId = slotId;
            this.stack = stack;
            this.dragType = dragType;
            this.clickType = clickType;
        }
    }
}
