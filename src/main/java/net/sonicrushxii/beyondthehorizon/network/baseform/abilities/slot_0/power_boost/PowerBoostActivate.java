package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModItems;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;

public class PowerBoostActivate implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PowerBoostActivate> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("beyondthehorizon", "power_boost_activate"));

    public static final StreamCodec<FriendlyByteBuf, PowerBoostActivate> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> msg.encode(buf), PowerBoostActivate::new);

    public PowerBoostActivate() {}

    public PowerBoostActivate(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void performPowerBoostActivate(ServerPlayer player)
    {
        //World
        Level world = player.level();

        PlayerSonicForm playerSonicForm = player.getData(ModAttachments.PLAYER_SONIC_FORM);
        BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

        //Equip Head
        if(baseformProperties.lightSpeedState != (byte)2)
        {
            Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
            armorItems.next(); armorItems.next();
            try {
                if (armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 1) {
                    ItemStack itemToPlace = new ItemStack(ModItems.BASEFORM_POWERBOOST_CHESTPLATE.get());
                    itemToPlace.setTag(BaseformProperties.baseformArmorNBTTag);
                    player.setItemSlot(EquipmentSlot.CHEST, itemToPlace);
                }
            }
            catch(NullPointerException ignored){}

            try{
                if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2){
                    EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                    player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformPBSonicHead);
                }
            }
            catch(NullPointerException ignored){}

            player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformPBSonicHead);
        }

        //Power Boost Data
        baseformProperties.powerBoost = true;
        player.setDeltaMovement(0.0,0.0,0.0);
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
        player.addEffect(new MobEffectInstance(ModEffects.INITATE_POWER_BOOST.get(),6,0,false,false,false));

        //Perform Blast
        {
            //Commands
            CommandSourceStack commandSourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
            MinecraftServer server = player.serverLevel().getServer();
            server.
                    getCommands().
                    performPrefixedCommand(commandSourceStack,"summon firework_rocket ~ ~ ~ {Life:0,LifeTime:0,FireworksItem:{id:\"firework_rocket\",Count:1,tag:{Fireworks:{Explosions:[{Type:4,Flicker:1b,Colors:[I;255,16777215],FadeColors:[I;65535,65535]}]}}}}");
        }

        //Add Speed Multiplier
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttributeMultipliers.POWERBOOST_SPEED))
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttributeMultipliers.POWERBOOST_SPEED);

        //Add Armor Multiplier
        if(!player.getAttribute(Attributes.ARMOR).hasModifier(AttributeMultipliers.POWERBOOST_ARMOR))
            player.getAttribute(Attributes.ARMOR).addTransientModifier(AttributeMultipliers.POWERBOOST_ARMOR);

        /*player.removeEffect(MobEffects.JUMP);
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, -1, 2, false, false));*/

        /*player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 3, false, false));*/

        //Strength
        player.removeEffect(MobEffects.DAMAGE_BOOST);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 2, false, false));

        //Haste
        player.removeEffect(MobEffects.DIG_SPEED);
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, -1, 3, false, false));

        //Sound
        world.playSound(null,player.getX(),player.getY(),player.getZ(), ModSounds.POWER_BOOST.get(), SoundSource.MASTER, 1.0f, 1.0f);

        PacketHandler.sendToALLPlayers(
                new SyncPlayerFormS2C(
                        player.getId(),
                        playerSonicForm
                ));
    }

    public static void handle(PowerBoostActivate msg, IPayloadContext ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if(player != null)
                        performPowerBoostActivate(player);
                });
    }
}
