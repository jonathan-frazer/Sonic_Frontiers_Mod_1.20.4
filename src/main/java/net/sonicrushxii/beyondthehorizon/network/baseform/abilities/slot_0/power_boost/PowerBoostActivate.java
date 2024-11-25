package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.power_boost;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.modded.ModEffects;
import net.sonicrushxii.beyondthehorizon.modded.ModSounds;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.baseform.passives.AttributeMultipliers;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.Iterator;

public class PowerBoostActivate {
    public PowerBoostActivate() {}

    public PowerBoostActivate(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer){

    }

    public static void performPowerBoostActivate(ServerPlayer player)
    {
        //World
        Level world = player.level();

        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Equip Head
            if(baseformProperties.lightSpeedState != (byte)2)
            {
                Iterator<ItemStack> armorItems = player.getArmorSlots().iterator();
                armorItems.next(); armorItems.next(); armorItems.next();
                try{
                    if(armorItems.next().getTag().getByte("BeyondTheHorizon") == (byte) 2){
                        EquipmentChangeHandler.playerHeadEquipmentLock.put(player.getUUID(),true);
                        player.setItemSlot(EquipmentSlot.HEAD, BaseformProperties.baseformPBSonicHead);
                    }
                }
                catch(NullPointerException ignored){}
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

            PacketHandler.sendToPlayer(player,
                    new SyncPlayerFormS2C(
                            playerSonicForm.getCurrentForm(),
                            baseformProperties
                    ));
        });
    }

    public void handle(CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(
                ()->{
                    ServerPlayer player = ctx.getSender();
                    if(player != null)
                        performPowerBoostActivate(player);
                });
        ctx.setPacketHandled(true);
    }
}
