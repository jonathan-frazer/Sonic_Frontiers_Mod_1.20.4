package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

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
            // This code is run on the client side
            Minecraft mc = Minecraft.getInstance();
            ClientLevel world = mc.level;
            LocalPlayer player = mc.player;

            if(player != null && world != null) {
                if(player.blockPosition().distSqr(emitterPosition) < 576)
                    world.playLocalSound(emitterPosition.getX(),emitterPosition.getY(),emitterPosition.getZ(),
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(this.soundLocation)),
                        SoundSource.MASTER, volume, pitch, true);
            }
        }));
        ctx.setPacketHandled(true);
    }
}
