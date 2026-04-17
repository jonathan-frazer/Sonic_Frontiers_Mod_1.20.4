package net.sonicrushxii.beyondthehorizon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.sonicrushxii.beyondthehorizon.KeyBindings;

public class VirtualSlotHandler {
    // 4 scrollable slots: 0=Boost, 1=Combo, 2=Ultimate, 3=Transformation
    // 2 toggle slots: 4=Melee (Mouse4), 5=Ranged (Mouse5)
    public static final byte SCROLLABLE_SLOT_COUNT = 4;
    public static final byte TOTAL_SLOT_COUNT = 6;
    public static final byte SLOT_BOOST = 0;
    public static final byte SLOT_COMBO = 1;
    public static final byte SLOT_ULTIMATE = 2;
    public static final byte SLOT_TRANSFORMATION = 3;
    public static final byte SLOT_MELEE = 4;
    public static final byte SLOT_RANGED = 5;

    private static byte currAbility;

    public static void initialize()
    {
        currAbility = SLOT_BOOST;
    }
    public static void setSlot(byte slotNum) {
        currAbility = (byte)(slotNum % TOTAL_SLOT_COUNT);
    }

    public static byte getCurrAbility() {return currAbility;}

    public static byte getSlotLength() {return TOTAL_SLOT_COUNT;}

    // Cycles only through the 4 scrollable slots (0-3) via Middle Mouse Button
    public static void cycleScrollableSlot() {
        if (currAbility >= SCROLLABLE_SLOT_COUNT) {
            // If in Melee or Ranged, cycle back to Combo first
            currAbility = SLOT_COMBO;
        } else {
            currAbility = (byte) ((currAbility + 1) % SCROLLABLE_SLOT_COUNT);
        }
    }

    // Toggle Melee slot (Mouse 4): go to Melee, or return to Combo if already there
    public static void toggleMeleeSlot() {
        if (currAbility == SLOT_MELEE) {
            currAbility = SLOT_COMBO;
        } else {
            currAbility = SLOT_MELEE;
        }
    }

    // Toggle Ranged slot (Mouse 5): go to Ranged, or return to Combo if already there
    public static void toggleRangedSlot() {
        if (currAbility == SLOT_RANGED) {
            currAbility = SLOT_COMBO;
        } else {
            currAbility = SLOT_RANGED;
        }
    }

    public static void goToSlot(byte targetSlot)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        AbstractClientPlayer player = mc.player;

        if (player != null && world != null) {
            VirtualSlotHandler.setSlot(targetSlot);
            consumeAllAbilityClicks();
        }
    }

    public static void consumeAllAbilityClicks() {
        while(KeyBindings.INSTANCE.useAbility1.consumeClick());
        while(KeyBindings.INSTANCE.useAbility2.consumeClick());
        while(KeyBindings.INSTANCE.useAbility3.consumeClick());
        while(KeyBindings.INSTANCE.useAbility4.consumeClick());
        while(KeyBindings.INSTANCE.useAbility5.consumeClick());
        while(KeyBindings.INSTANCE.useAbility6.consumeClick());
    }
}