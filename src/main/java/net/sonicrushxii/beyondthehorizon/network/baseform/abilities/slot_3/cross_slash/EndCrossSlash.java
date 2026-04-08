package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.cross_slash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformActiveAbility;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class EndCrossSlash implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<EndCrossSlash> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "end_cross_slash"));

    public static final StreamCodec<FriendlyByteBuf, EndCrossSlash> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), EndCrossSlash::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public EndCrossSlash() {}

    public EndCrossSlash(FriendlyByteBuf buffer){}

    public void encode(FriendlyByteBuf buffer){}

    public static void performEndCrossSlash(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Reset Counter to 0
        baseformProperties.crossSlash = 0;
        //Return Gravity
        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY).setBaseValue(0.08);
        //Cooldown
        baseformProperties.setCooldown(BaseformActiveAbility.CROSS_SLASH, (byte) 5);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(EndCrossSlash msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null){
                        performEndCrossSlash(player);
                    }
                });
    }
}
