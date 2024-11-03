package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.AutoStep;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class StartSprint {

    public StartSprint() {}

    public StartSprint(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void sonicBoomEffect(ServerPlayer player) {
        Level world = player.level();
        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.MAX_BOOST.get(), SoundSource.MASTER, 1.0f, 1.0f);

        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(ParticleTypes.FLASH, player.getX()+0.0, player.getY()+1.0, player.getZ()+0.0,
                0.001, 0.0f, 1, true));
        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(ParticleTypes.SONIC_BOOM, player.getX()+0.0, player.getY()+1.0, player.getZ()+0.0,
                0.001, 0.0f, 1, true));
    }

    public static void performStartSprint(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
            BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
            baseformProperties.sprintFlag = true;

            //Activate Auto Step
            AutoStep.performStepUpActivate(player);

            //Apply Boost Levels
            switch(baseformProperties.boostLvl)
            {
                case 0: player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                        break;
                case 1: player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.75);
                        break;
                case 2: player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.00);
                        break;
                case 3: if(!baseformProperties.boosted) sonicBoomEffect(player);
                        baseformProperties.boosted = true;
                        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.25);
                        break;
            }

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        StartSprint.performStartSprint(player);
                });
        ctx.setPacketHandled(true);
    }
}
