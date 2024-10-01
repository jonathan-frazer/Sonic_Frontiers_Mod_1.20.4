package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.light_speed_attack;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.function.Supplier;

public class LightspeedCharge {

    public LightspeedCharge() {}

    public LightspeedCharge(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performLightspeedCharge(ServerPlayer player)
    {
        //Add Tag
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.lightSpeedState = (byte)1;

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });

        //Slow Down
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 66, 22, false, false));

        Level world = player.level();
        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.LIGHT_SPEED_CHARGE.get(), SoundSource.MASTER, 1.0f, 1.0f);
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        performLightspeedCharge(player);

                });
        ctx.setPacketHandled(true);
    }
}

