package net.sonicrushxii.beyondthehorizon.network.baseform.passives.wall_boost;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.ModUtils;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Objects;

public class WallBoost {
    public WallBoost() {}

    public WallBoost(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performWallBoost(ServerPlayer player, BaseformProperties baseformProperties)
    {
        //Move Upward
        player.setSprinting(false);
        baseformProperties.wallBoosting = true;
        Objects.requireNonNull(player.getAttribute(ForgeMod.ENTITY_GRAVITY.get())).setBaseValue(0.0);
        player.setDeltaMovement(new Vec3(0, Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 2.5, 0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                    {
                        Vec3 playerDirCentre = ModUtils.calculateViewVector(0.0f, player.getViewYRot(0)).scale(0.75);
                        BlockPos centrePos = player.blockPosition().offset(
                                (int) Math.round(playerDirCentre.x),
                                (Math.round(player.getY()) > player.getY()) ? 1 : 0,
                                (int) Math.round(playerDirCentre.z)
                        );

                        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm->{
                            BaseformProperties baseformProperties =  (BaseformProperties) playerSonicForm.getFormProperties();
                            //Wall Boost
                            if (!ModUtils.passableBlocks.contains(ForgeRegistries.BLOCKS.getKey(player.level().getBlockState(centrePos.offset(0, 1, 0)).getBlock()) + "")
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
                        });
                    }
                });
        ctx.setPacketHandled(true);
    }
}

