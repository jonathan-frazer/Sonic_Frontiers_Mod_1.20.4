package net.sonicrushxii.beyondthehorizon.event_handler.client_handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitzOff;

@EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, value = Dist.CLIENT)
public class InputSlotHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = mc.player;

        if (player == null || !player.isAlive()) return;
        if (mc.isPaused() || mc.screen != null) return;

        PlayerSonicForm psf = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        if (psf.getCurrentForm() == SonicForm.PLAYER) return;

        double delta = event.getScrollDeltaY();
        if (delta == 0.0) return;

        try {
            if (psf.getFormProperties() instanceof BaseformProperties bp
                    && bp.speedBlitz
                    && VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_COMBO) {
                PacketHandler.sendToServer(new SpeedBlitzOff());
            }
        } catch (NullPointerException ignored) {}

        VirtualSlotHandler.cycleScrollableSlot(delta > 0);
        VirtualSlotHandler.consumeAllAbilityClicks();
        event.setCanceled(true);
    }
}
