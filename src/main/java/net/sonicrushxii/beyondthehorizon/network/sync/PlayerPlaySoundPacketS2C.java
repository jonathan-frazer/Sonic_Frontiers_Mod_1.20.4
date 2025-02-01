package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.sonicrushxii.beyondthehorizon.event_handler.client_handlers.ClientPacketHandler;

public class PlayerPlaySoundPacketS2C
{
    private final ResourceLocation soundLocation;
    private final BlockPos emitterPosition;
    private final float volume;
    private final float pitch;

    public PlayerPlaySoundPacketS2C(BlockPos emitterPosition, ResourceLocation soundLocation) {
        this.soundLocation = soundLocation;
        this.emitterPosition = emitterPosition;
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    public PlayerPlaySoundPacketS2C(BlockPos emitterPosition, ResourceLocation soundLocation, float volume) {
        this.soundLocation = soundLocation;
        this.emitterPosition = emitterPosition;
        this.volume = volume;
        this.pitch = 1.0f;
    }

    public PlayerPlaySoundPacketS2C(BlockPos emitterPosition, ResourceLocation soundLocation, float volume, float pitch) {
        this.soundLocation = soundLocation;
        this.emitterPosition = emitterPosition;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PlayerPlaySoundPacketS2C(FriendlyByteBuf buf) {
        this.soundLocation = buf.readResourceLocation();
        this.emitterPosition = buf.readBlockPos();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.soundLocation);
        buf.writeBlockPos(this.emitterPosition);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientPacketHandler.clientPlaysound(soundLocation,emitterPosition,volume,pitch);
        }));
        ctx.setPacketHandled(true);
    }
}
