package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.spindash;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class SpindashBreak {
    private static final int RADIUS = 1;

    public SpindashBreak() {    }

    public SpindashBreak(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
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
                                if(!Utilities.unbreakableBlocks.contains(ForgeRegistries.BLOCKS.getKey(blockState.getBlock())+""))
                                    player.level().destroyBlock(pos,true);
                            }

                            PacketHandler.sendToALLPlayers(
                                    new SyncPlayerFormS2C(
                                            player.getId(),
                                            playerSonicForm
                                    ));
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}
