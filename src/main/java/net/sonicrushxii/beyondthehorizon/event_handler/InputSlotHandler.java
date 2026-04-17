package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitzOff;

@EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class InputSlotHandler {

    // Slot cycling is now handled via Middle Mouse Button (cycleSlotKey) in ClientTickHandler.
    // Mouse 4 toggles Melee slot; Mouse 5 toggles Ranged slot — also handled in ClientTickHandler.

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = mc.player;

        if (player == null || !player.level().isClientSide())
            return;

        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        if (playerSonicForm.getCurrentForm() == SonicForm.PLAYER)
            return;

        FormProperties formProperties = playerSonicForm.getFormProperties();

        // Cycle slots: Middle Mouse Button click
        if (KeyBindings.INSTANCE.cycleSlotKey.consumeClick()) {
            try {
                BaseformProperties bp = (BaseformProperties) formProperties;
                if (bp.speedBlitz && VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_COMBO) {
                    PacketHandler.sendToServer(new SpeedBlitzOff());
                }
            } catch (ClassCastException | NullPointerException ignored) {}

            VirtualSlotHandler.cycleScrollableSlot();
            VirtualSlotHandler.consumeAllAbilityClicks();
        }

        // Toggle Melee slot: Mouse 4
        if (KeyBindings.INSTANCE.meleeSlotKey.consumeClick()) {
            try {
                BaseformProperties bp = (BaseformProperties) formProperties;
                if (bp.speedBlitz && VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_COMBO) {
                    PacketHandler.sendToServer(new SpeedBlitzOff());
                }
            } catch (ClassCastException | NullPointerException ignored) {}

            VirtualSlotHandler.toggleMeleeSlot();
            VirtualSlotHandler.consumeAllAbilityClicks();
        }

        // Toggle Ranged slot: Mouse 5
        if (KeyBindings.INSTANCE.rangedSlotKey.consumeClick()) {
            try {
                BaseformProperties bp = (BaseformProperties) formProperties;
                if (bp.speedBlitz && VirtualSlotHandler.getCurrAbility() == VirtualSlotHandler.SLOT_COMBO) {
                    PacketHandler.sendToServer(new SpeedBlitzOff());
                }
            } catch (ClassCastException | NullPointerException ignored) {}

            VirtualSlotHandler.toggleRangedSlot();
            VirtualSlotHandler.consumeAllAbilityClicks();
        }
    }
}
