package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_3.sonic_boom;

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
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.PlayerStopSoundPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class EndSonicBoom implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<EndSonicBoom> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "end_sonic_boom"));

    public static final StreamCodec<FriendlyByteBuf, EndSonicBoom> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), EndSonicBoom::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public EndSonicBoom() {

    }

    public EndSonicBoom(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){
    }

    public static void performEndSonicBoom(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Reset Counter to 0
        baseformProperties.sonicBoom = 0;
        //Return Gravity
        player.getAttribute(NeoForgeMod.ENTITY_GRAVITY).setBaseValue(0.08);
        //Cooldown
        baseformProperties.setCooldown(BaseformActiveAbility.SONIC_BOOM, (byte) 5);
        //Play Sound
        PacketHandler.sendToALLPlayers(new PlayerStopSoundPacketS2C(ModSounds.SONIC_BOOM.get().getLocation()));

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }


    public static void handle(EndSonicBoom msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performEndSonicBoom(player);
                });
    }
}
