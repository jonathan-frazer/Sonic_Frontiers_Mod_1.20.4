package net.sonicrushxii.beyondthehorizon.network.baseform.passives.wall_boost;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Objects;

public class WallBoost implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WallBoost> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "wall_boost"));

    public static final StreamCodec<FriendlyByteBuf, WallBoost> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), WallBoost::new);

    public WallBoost() {}

    public WallBoost(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performWallBoost(ServerPlayer player, BaseformProperties baseformProperties)
    {
        //Move Upward
        player.setSprinting(false);
        baseformProperties.wallBoosting = true;
        Objects.requireNonNull(player.getAttribute(Attributes.GRAVITY)).setBaseValue(0.0);
        player.setDeltaMovement(new Vec3(0, Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 2.5, 0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public static void handle(WallBoost msg, IPayloadContext ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        Vec3 playerDirCentre = ModUtils.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
                        BlockPos centrePos = player.blockPosition().offset(
                                (int) Math.round(playerDirCentre.x),
                                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                                (int) Math.round(playerDirCentre.z)
                        );

                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
                        //Wall Boost
                        if (!ModUtils.passableBlocks.contains(BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(centrePos.offset(0, 1, 0)).getBlock()) + "")
                                && baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3
                                && player.isSprinting())
                        {
                            WallBoost.performWallBoost(player,baseformProperties);
                            PacketHandler.sendToALLPlayers(
                                    new SyncPlayerFormS2C(
                                            player.getId(),
                                            playerSonicForm
                                    ));
                        }
                    }
                });
    }
}

