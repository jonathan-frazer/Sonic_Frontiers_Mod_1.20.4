package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.test.TestPacket;


public class PlayerTickHandler {
    private static int tickCounter = 0;
    private static final int TICKS_PER_SECOND = 20;
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent.Pre event) {
        if (event.player == null) return;
        if (!event.player.level().isClientSide()) return;

        LocalPlayer player = (LocalPlayer) event.player;

        // Check if the player is valid and fully initialized
        if (!player.isAlive()) {
            return;
        }

        //Helps Run OnClientSecond
        ++tickCounter;
        if (tickCounter >= TICKS_PER_SECOND) {
            tickCounter = 0;
        }

        //Test Sending a Packet
        if(KeyBindings.INSTANCE.useAbility1.consumeClick()){
            PacketHandler.sendToServer(new TestPacket());
        }
    }
}
