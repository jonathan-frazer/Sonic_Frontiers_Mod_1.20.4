package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.boost;

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
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.StartSprint;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;


public class Boost implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<Boost> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "boost"));

    public static final StreamCodec<FriendlyByteBuf, Boost> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), Boost::new);

    private final boolean wasShiftDown;

    public Boost(boolean wasShiftDown) {
        this.wasShiftDown = wasShiftDown;
    }

    public Boost(FriendlyByteBuf buffer) {
        this.wasShiftDown = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.wasShiftDown);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performBoost(ServerPlayer player, boolean wasShiftDown)
    {
        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();
        if(!wasShiftDown) baseformProperties.boostLvl = (byte)((baseformProperties.boostLvl+1)%4);
        else              baseformProperties.boostLvl = (byte)((baseformProperties.boostLvl==0)?3: baseformProperties.boostLvl-1);

        //Boost Level 3
        if(baseformProperties.boostLvl == 3 && !player.isSprinting()) baseformProperties.boosted = false;

        if(player.isSprinting())
            switch(baseformProperties.boostLvl)
            {
                case 0 : player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
                         break;
                case 1 : player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.75);
                         break;
                case 2 : player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.00);
                         break;
                case 3 : StartSprint.sonicBoomEffect(player);
                         player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.25);
                         break;
            }

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(Boost msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performBoost(player,msg.wasShiftDown);
                });
    }
}