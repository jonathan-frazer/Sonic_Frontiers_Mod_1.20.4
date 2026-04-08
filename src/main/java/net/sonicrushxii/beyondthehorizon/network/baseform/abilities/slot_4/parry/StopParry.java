package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_4.parry;

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
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.GoToVirtualSlotS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.UUID;

public class StopParry implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<StopParry> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "stop_parry"));

    public static final StreamCodec<FriendlyByteBuf, StopParry> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), StopParry::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public StopParry() {

    }

    public StopParry(FriendlyByteBuf buffer){

    }

    public void encode(FriendlyByteBuf buffer){
    }

    public static void performStopParry(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Reset Counter to 0
        baseformProperties.parryTime = -60;
        //Return Gravity
        player.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);

        //Remove Movement Speed Modifier
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_HOLD.id()))
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_HOLD.id());

        //Play Sound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.MASTER, 1.0f, 1.0f);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void performParrySuccess(ServerPlayer player, UUID parryTargetId)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Move to Counter Slot
        PacketHandler.sendToPlayer(player,new GoToVirtualSlotS2C((byte)4));

        performStopParry(player);

        //Perform Timeslow
        baseformProperties.counterReady = true;
        baseformProperties.parryTimeSlow = 1;
        baseformProperties.counteredEntity = parryTargetId;
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_SPEED.id()))
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.PARRY_SPEED);

        //Play Sound
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.PARRY.get(), SoundSource.MASTER, 0.75f, 1.0f);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void returnFromParryTime(ServerPlayer player)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Reset Data
        baseformProperties.parryTimeSlow = 0;

        //Stop Timeslow
        baseformProperties.counterReady = false;
        baseformProperties.counteredEntity = new UUID(0L,0L);

        //Remove Attributes
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.PARRY_SPEED.id()))
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttributeMultipliers.PARRY_SPEED.id());

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }


    public static void handle(StopParry msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performStopParry(player);
                });
    }
}
