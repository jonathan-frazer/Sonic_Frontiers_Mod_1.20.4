package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.sonicrushxii.beyondthehorizon.KeyBindings;

public class VirtualSlotHandler {
    private static byte NO_OF_SLOTS;
    private static byte currAbility;

    public static void initialize(byte slotLength)
    {
        currAbility = 0;
        NO_OF_SLOTS = slotLength;
    }
    public static void setSlot(byte slotNum) {
        currAbility = (byte)(slotNum% NO_OF_SLOTS);
    }

    public static byte getCurrAbility() {return currAbility;}

    public static byte getSlotLength() {return NO_OF_SLOTS;}

    public static void scrollUpByOne() { currAbility = (byte) ((currAbility+1)% NO_OF_SLOTS); }
    public static void scrollDownByOne() {
        if(currAbility == 0)
            currAbility = (byte)(NO_OF_SLOTS -1);
        else
            currAbility = (byte)(currAbility-1);
    }

    public static void goToSlot(byte targetSlot)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if (player != null && world != null) {
            VirtualSlotHandler.setSlot(targetSlot);
            //Consume All clicks before switching over to another slot
            while(KeyBindings.INSTANCE.useAbility1.consumeClick());
            while(KeyBindings.INSTANCE.useAbility2.consumeClick());
            while(KeyBindings.INSTANCE.useAbility3.consumeClick());
            while(KeyBindings.INSTANCE.useAbility4.consumeClick());
            while(KeyBindings.INSTANCE.useAbility5.consumeClick());
            while(KeyBindings.INSTANCE.useAbility6.consumeClick());
        }
    }
}