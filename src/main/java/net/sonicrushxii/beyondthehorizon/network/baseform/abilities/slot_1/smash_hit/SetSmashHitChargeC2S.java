package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.smash_hit;

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
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class SetSmashHitChargeC2S implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetSmashHitChargeC2S> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "set_smash_hit_charge_c2s"));

    public static final StreamCodec<FriendlyByteBuf, SetSmashHitChargeC2S> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), SetSmashHitChargeC2S::new);

    private final byte newChargeAmt;

    public SetSmashHitChargeC2S(byte newChargeAmt) {
        this.newChargeAmt = newChargeAmt;
    }

    public SetSmashHitChargeC2S(FriendlyByteBuf buffer) {
        this.newChargeAmt = buffer.readByte();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(this.newChargeAmt);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetSmashHitChargeC2S msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                    {
                        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

                        // Smash Attack no longer slows player while charging — remove any leftover modifier
                        if(player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.SMASH_HIT.id()))
                            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.SMASH_HIT.id());

                        baseformProperties.smashHit = msg.newChargeAmt;

                        PacketHandler.sendToALLPlayers(
                                new SyncPlayerFormS2C(
                                        player.getId(),
                                        playerSonicForm
                                ));
                    }
                });
    }
}
