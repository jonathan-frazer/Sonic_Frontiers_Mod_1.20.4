package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class BoostAuraToggle implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BoostAuraToggle> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "boost_aura_toggle"));

    public static final StreamCodec<FriendlyByteBuf, BoostAuraToggle> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), BoostAuraToggle::new);

    public BoostAuraToggle() {}
    public BoostAuraToggle(FriendlyByteBuf buffer) {}
    public void encode(FriendlyByteBuf buffer) {}

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(BoostAuraToggle msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player != null) {
                PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
                if (!(playerSonicForm.getFormProperties() instanceof BaseformProperties baseformProperties)) return;

                baseformProperties.boostAura = !baseformProperties.boostAura;

                if (baseformProperties.boostAura) {
                    if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.BOOST_AURA_SPEED.id()))
                        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.BOOST_AURA_SPEED);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BEACON_ACTIVATE, SoundSource.MASTER, 0.8f, 1.5f);
                } else {
                    if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.BOOST_AURA_SPEED.id()))
                        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.BOOST_AURA_SPEED.id());
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 0.8f, 1.5f);
                }

                PacketHandler.sendToALLPlayers(new SyncPlayerFormS2C(player.getId(), playerSonicForm));
            }
        });
    }
}
