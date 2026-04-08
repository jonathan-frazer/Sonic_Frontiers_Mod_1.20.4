package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class SpindashBreak implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SpindashBreak> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "spindash_break"));

    public static final StreamCodec<FriendlyByteBuf, SpindashBreak> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SpindashBreak::new);

    private static final int RADIUS = 1;

    public SpindashBreak() {    }

    public SpindashBreak(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SpindashBreak msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        //Define Player Positions
                        Vec3 lookAngle = player.getLookAngle().scale(1.0);
                        BlockPos playerPos = new BlockPos(
                                (int)(player.getX()+lookAngle.x()),
                                (int)(player.getY()),
                                (int)(player.getZ()+lookAngle.z())
                        );

                        BlockPos start = playerPos.offset(-(RADIUS+1), RADIUS+1, -(RADIUS+1));
                        BlockPos end = playerPos.offset(0, 0, 0);

                        // Use BlockPos.betweenClosed to iterate over all positions in the cube
                        for (BlockPos pos : BlockPos.betweenClosed(start, end))
                        {
                            BlockState blockState = player.level().getBlockState(pos);
                            if(!ModUtils.unbreakableBlocks.contains(BuiltInRegistries.BLOCK.getKey(blockState.getBlock())+""))
                                player.level().destroyBlock(pos,true);
                        }

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
