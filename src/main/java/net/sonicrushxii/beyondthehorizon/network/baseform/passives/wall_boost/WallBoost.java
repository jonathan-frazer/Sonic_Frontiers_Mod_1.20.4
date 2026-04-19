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
    public WallBoost(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    // 0=none, 1=N(-Z), 2=S(+Z), 3=E(+X), 4=W(-X), 5=ceiling
    public static Vec3 surfaceNormal(byte surface) {
        return switch (surface) {
            case 1 -> new Vec3(0, 0, 1);
            case 2 -> new Vec3(0, 0, -1);
            case 3 -> new Vec3(-1, 0, 0);
            case 4 -> new Vec3(1, 0, 0);
            case 5 -> new Vec3(0, -1, 0);
            default -> Vec3.ZERO;
        };
    }

    public static boolean isSolid(ServerPlayer player, BlockPos pos) {
        return !ModUtils.passableBlocks.contains(
            BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(pos).getBlock()) + "");
    }

    // Picks the wall/ceiling surface the player should run on.
    public static byte detectSurface(ServerPlayer player) {
        BlockPos pp = player.blockPosition();
        // {dx, dz} for N, S, E, W
        int[][] dirs = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};

        Vec3 look = player.getLookAngle();
        Vec3 lookH = new Vec3(look.x, 0, look.z);
        if (lookH.lengthSqr() > 0.001) lookH = lookH.normalize();

        byte best = 0;
        double bestDot = 0.3; // minimum dot threshold to count as "facing the wall"

        for (int i = 0; i < 4; i++) {
            int dx = dirs[i][0], dz = dirs[i][1];
            boolean solidLow  = isSolid(player, pp.offset(dx, 0, dz));
            boolean solidHigh = isSolid(player, pp.offset(dx, 1, dz));
            if (solidLow || solidHigh) {
                double dot = lookH.x * dx + lookH.z * dz;
                if (dot > bestDot) {
                    bestDot = dot;
                    best = (byte)(i + 1);
                }
            }
        }

        if (best != 0) return best;

        // Ceiling fallback
        if (isSolid(player, pp.offset(0, 2, 0))) return 5;
        return 0;
    }

    public static void performWallBoost(ServerPlayer player, BaseformProperties baseformProperties) {
        byte surface = detectSurface(player);
        if (surface == 0) return;

        baseformProperties.wallRunSurface = surface;
        baseformProperties.wallBoosting = true;
        player.setSprinting(false);
        Objects.requireNonNull(player.getAttribute(Attributes.GRAVITY)).setBaseValue(0.0);

        Vec3 look = player.getLookAngle();
        Vec3 normal = surfaceNormal(surface);
        Vec3 proj = look.subtract(normal.scale(look.dot(normal)));
        if (proj.lengthSqr() < 0.001) proj = new Vec3(0, 1, 0);
        double speed = Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 2.5;
        player.setDeltaMovement(proj.normalize().scale(speed));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    public static void handle(WallBoost msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;

            PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            if (baseformProperties.boostLvl >= 1 && baseformProperties.boostLvl <= 3
                    && player.isSprinting() && !baseformProperties.wallBoosting) {
                WallBoost.performWallBoost(player, baseformProperties);
                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
            }
        });
    }
}
