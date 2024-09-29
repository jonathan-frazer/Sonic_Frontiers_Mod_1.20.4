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

public class PlayerStopSoundPacketS2C {
    private final ResourceLocation soundLocation;

    public PlayerStopSoundPacketS2C(ResourceLocation soundLocation) {
        this.soundLocation = soundLocation;
    }

    public PlayerStopSoundPacketS2C(FriendlyByteBuf buf) {
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
                mc.getSoundManager().stop(this.soundLocation, SoundSource.MASTER);
            }
        }));
        ctx.setPacketHandled(true);
    }
}
