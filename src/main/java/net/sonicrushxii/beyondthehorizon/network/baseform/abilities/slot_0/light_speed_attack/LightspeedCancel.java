package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class LightspeedCancel {
    public LightspeedCancel() {}

    public LightspeedCancel(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performLightspeedCancel(ServerPlayer player)
    {
        Level world = player.level();

        //Remove Slowdown
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

        //Cancel the Sound
        PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(ModSounds.LIGHT_SPEED_CHARGE.get().getLocation()));
        world.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 1.0f, 1.0f);

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.lightSpeedState = (byte)0;

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        performLightspeedCancel(player);
                });
        ctx.setPacketHandled(true);
    }
}
