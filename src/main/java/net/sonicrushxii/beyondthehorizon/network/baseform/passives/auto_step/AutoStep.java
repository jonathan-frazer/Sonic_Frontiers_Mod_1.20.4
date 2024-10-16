package net.sonicrushxii.beyondthehorizon.network.baseform.passives.auto_step;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.Utilities;

public class AutoStep {
    public static void performStepUpActivate(ServerPlayer player) {
        player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(1.5);
    }

    public static void performStepUpDeactivate(ServerPlayer player) {
        player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0.0);
    }

    public static void performStepDown(ServerPlayer player, double stepDownHeight)
    {
        Vec3 playerDir = player.getLookAngle().scale(1);
        Vec3 pos = new Vec3(
                player.getX()+playerDir.x,
                player.getY()-stepDownHeight,
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
}
