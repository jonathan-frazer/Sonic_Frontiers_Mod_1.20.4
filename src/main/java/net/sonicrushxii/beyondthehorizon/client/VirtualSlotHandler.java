package net.sonicrushxii.beyondthehorizon.client;

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
}