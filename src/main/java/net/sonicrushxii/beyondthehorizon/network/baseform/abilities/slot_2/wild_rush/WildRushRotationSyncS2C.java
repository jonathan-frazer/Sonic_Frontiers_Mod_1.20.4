package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_2.wild_rush;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformClient;

public class WildRushRotationSyncS2C {
    private float yaw;
    private float pitch;

    public WildRushRotationSyncS2C(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public WildRushRotationSyncS2C(FriendlyByteBuf buf) {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            BaseformClient.ClientOnlyData.wildRushYawPitch[0] = yaw;
            BaseformClient.ClientOnlyData.wildRushYawPitch[1] = pitch;
        }));
        ctx.setPacketHandled(true);
    }
}
