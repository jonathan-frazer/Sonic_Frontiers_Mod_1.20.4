package net.sonicrushxii.beyondthehorizon.network.baseform.passives;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step.AutoStep;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class StopSprint implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StopSprint> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "stop_sprint"));

    public static final StreamCodec<FriendlyByteBuf, StopSprint> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), StopSprint::new);

    public StopSprint() {}

    public StopSprint(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performStopSprint(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
        baseformProperties.sprintFlag = false;

        //Undo Boost
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.50);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));

        //Activate Auto Step
        AutoStep.performStepUpDeactivate(player);
    }

    public static void handle(StopSprint msg, IPayloadContext ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        performStopSprint(player);
                    }
                });
    }
}
