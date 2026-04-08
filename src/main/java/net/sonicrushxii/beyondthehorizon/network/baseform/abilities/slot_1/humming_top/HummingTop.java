package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.humming_top;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerPlaySoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class HummingTop implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<HummingTop> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "humming_top"));

    public static final StreamCodec<FriendlyByteBuf, HummingTop> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), HummingTop::new);

    private final boolean activate;

    public HummingTop(boolean activate) {
        this.activate = activate;
    }

    public HummingTop(FriendlyByteBuf buffer) {
        this.activate = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.activate);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void hummingTopActivate(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Gravity
        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.0);
        //Modify Data
        baseformProperties.hummingTop = 1;

        //Play Sound
        PacketHandler.sendToALLPlayers(new PlayerPlaySoundPacketS2C(player.blockPosition(),
                ModSounds.HUMMING_TOP.get().getLocation())
        );

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void hummingTopEnd(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Gravity
        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);

        //Modify Data
        baseformProperties.hummingTop = 0;
        baseformProperties.ballFormState = 0;

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(HummingTop msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        if(msg.activate)   hummingTopActivate(player);
                        else                hummingTopEnd(player);
                    }
                });
    }
}
