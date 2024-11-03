package net.sonicrushxii.beyondthehorizon.event_handler;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.KeyBindings;
import net.sonicrushxii.beyondthehorizon.capabilities.SonicForm;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;

@Mod.EventBusSubscriber(modid = BeyondTheHorizon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputSlotHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        boolean isScrollingUp = (event.getDeltaY() >= 0);

        // Get the Minecraft instance and the player
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if(player == null || !player.level().isClientSide())
            return;

        // Baseform Slot Handler
        if (ClientFormData.getPlayerForm() != SonicForm.PLAYER &&
                KeyBindings.INSTANCE.virtualSlotUse.isDown())
        {
            // Cancel the event to prevent the hotbar from scrolling
            event.setCanceled(true);

            if(isScrollingUp)    VirtualSlotHandler.scrollUpByOne();
            else                 VirtualSlotHandler.scrollDownByOne();

            if(VirtualSlotHandler.getCurrAbility() == 0)
                while(KeyBindings.INSTANCE.useAbility1.consumeClick());
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        // Get the Minecraft instance and the player
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if(player == null || !player.level().isClientSide())
            return;

        // Baseform Slot Handler
        if (ClientFormData.getPlayerForm() != SonicForm.PLAYER &&
                KeyBindings.INSTANCE.virtualSlotUse.isDown())
        {
            int key = event.getKey();
            if (key >= InputConstants.KEY_1 && key <= InputConstants.KEY_9) {
                VirtualSlotHandler.setSlot((byte)(key-InputConstants.KEY_1));
                if(VirtualSlotHandler.getCurrAbility() == 0)
                    while(KeyBindings.INSTANCE.useAbility1.consumeClick());
            }
        }
    }
}
