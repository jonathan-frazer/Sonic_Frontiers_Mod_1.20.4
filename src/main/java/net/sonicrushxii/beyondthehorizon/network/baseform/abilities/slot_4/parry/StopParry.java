package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.GoToVirtualSlotS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.UUID;

public class StopParry
{
    public StopParry() {

    }

    public StopParry(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){
    }

    public static void performStopParry(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Reset Counter to 0
            baseformProperties.parryTime = -60;
            //Return Gravity
            player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.08);

            //Remove Movement Speed Modifier
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_HOLD))
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_HOLD.getId());

            //Play Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.MASTER, 1.0f, 1.0f);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public static void performParrySuccess(ServerPlayer player, UUID parryTargetId)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Move to Counter Slot
            PacketHandler.sendToPlayer(player,new GoToVirtualSlotS2C((byte)4));

            performStopParry(player);

            //Perform Timeslow
            baseformProperties.counterReady = true;
            baseformProperties.parryTimeSlow = 1;
            baseformProperties.counteredEntity = parryTargetId;
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_SPEED))
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.PARRY_SPEED);

            //Play Sound
            player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.PARRY.get(), SoundSource.MASTER, 0.75f, 1.0f);

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }

    public static void returnFromParryTime(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Reset Data
            baseformProperties.parryTimeSlow = 0;

            //Stop Timeslow
            baseformProperties.counterReady = false;
            baseformProperties.counteredEntity = new UUID(0L,0L);

            //Remove Attributes
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_SPEED))
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_SPEED.getId());

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
                    ));
        });
    }


    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        performStopParry(player);
                });
        ctx.setPacketHandled(true);
    }
}
