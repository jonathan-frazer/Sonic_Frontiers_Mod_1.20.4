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
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.client.ClientFormData;
import net.sonicrushxii.beyondthehorizon.client.VirtualSlotHandler;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.speed_blitz.SpeedBlitzOff;

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

            //If was on Second Slot then turn off Speed Blitz
            try
            {
                BaseformProperties baseformProperties = (BaseformProperties) ClientFormData.getPlayerFormDetails();
                if(baseformProperties.speedBlitz && VirtualSlotHandler.getCurrAbility() == (byte)1)
                {
                    PacketHandler.sendToServer(new SpeedBlitzOff());
                }
            }catch (ClassCastException|NullPointerException ignored){}


            if(isScrollingUp)    VirtualSlotHandler.scrollUpByOne();
            else                 VirtualSlotHandler.scrollDownByOne();

            //Consume All clicks before switching over to another slot
            while(KeyBindings.INSTANCE.useAbility1.consumeClick());
            while(KeyBindings.INSTANCE.useAbility2.consumeClick());
            while(KeyBindings.INSTANCE.useAbility3.consumeClick());
            while(KeyBindings.INSTANCE.useAbility4.consumeClick());
            while(KeyBindings.INSTANCE.useAbility5.consumeClick());
            while(KeyBindings.INSTANCE.useAbility6.consumeClick());
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
}
