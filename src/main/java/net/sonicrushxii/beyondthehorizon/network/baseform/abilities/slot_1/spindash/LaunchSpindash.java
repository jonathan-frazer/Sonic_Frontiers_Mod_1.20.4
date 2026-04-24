package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

public class LaunchSpindash implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LaunchSpindash> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "launch_spindash"));

    public static final StreamCodec<FriendlyByteBuf, LaunchSpindash> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), LaunchSpindash::new);

    public LaunchSpindash() {    }

    public LaunchSpindash(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performRevertSpindash(ServerPlayer player, BaseformProperties baseformProperties)
    {
        baseformProperties.ballFormState = (byte) 0;
        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);
        if (baseformProperties.boostLvl == 0 && !player.isSprinting())
            player.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(0.0);
    }

    public static void handle(LaunchSpindash msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        //Set Data -> Charging
                        baseformProperties.ballFormState = (byte)2;

                        //Enter Ball Form
                        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                        player.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.5);
                        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);

                        //PlaySound
                        Level world = player.level();
                        PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(ModSounds.SPINDASH_CHARGE.get().getLocation()));
                        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.SPINDASH_RELEASE.get(), SoundSource.MASTER, 1.0f, 1.0f);

                        //Always max duration per PDF
                        Scheduler.scheduleTask(()-> performRevertSpindash(player,baseformProperties), 100);

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}

