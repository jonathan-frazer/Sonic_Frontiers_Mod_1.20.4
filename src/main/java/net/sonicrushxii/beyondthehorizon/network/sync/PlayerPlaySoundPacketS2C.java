package net.sonicrushxii.beyondthehorizon.network.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class PlayerPlaySoundPacketS2C {
    private final ResourceLocation soundLocation;

    public PlayerPlaySoundPacketS2C(ResourceLocation soundLocation) {
        this.soundLocation = soundLocation;
    }

    public PlayerPlaySoundPacketS2C(FriendlyByteBuf buf) {
        this.soundLocation = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.soundLocation);
    }

    public void handle(CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // This code is run on the client side
            Minecraft mc = Minecraft.getInstance();
            ClientLevel world = mc.level;
            LocalPlayer player = mc.player;

            if(player != null && world != null) {
                world.playLocalSound(player.getX(),player.getY(),player.getZ(),
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(this.soundLocation)),
                        SoundSource.MASTER, 1.0f, 1.0f, true);
            }
        }));
        ctx.setPacketHandled(true);
    }
}
