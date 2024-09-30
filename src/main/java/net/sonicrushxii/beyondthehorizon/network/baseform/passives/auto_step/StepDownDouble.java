package net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;

import java.util.function.Supplier;

public class StepDownDouble {

    public StepDownDouble() {}

    public StepDownDouble(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null){
                        Vec3 playerDir = player.getLookAngle().scale(1);
                        Vec3 pos = new Vec3(
                                player.getX()+playerDir.x,
                                player.getY()-1.95,
                                player.getZ()+playerDir.z
                        );

                        ServerLevel world = player.serverLevel();
                        String destinationBlockName = ForgeRegistries.BLOCKS.getKey(
                                world.getBlockState(
                                        new BlockPos(
                                                (int)Math.round(pos.x),
                                                (int)Math.round(pos.y+1),
                                                (int)Math.round(pos.z)
                                        )).getBlock())+"";

                        if(Utilities.passableBlocks.contains(destinationBlockName))
                            player.teleportTo(pos.x,pos.y,pos.z);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
