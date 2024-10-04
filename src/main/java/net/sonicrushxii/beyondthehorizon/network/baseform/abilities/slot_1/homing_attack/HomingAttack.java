package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_1.homing_attack;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.event_handler.EquipmentChangeHandler;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class HomingAttack
{
    public HomingAttack() {    }

    public HomingAttack(FriendlyByteBuf buffer){    }

    public void encode(FriendlyByteBuf buffer){    }

    public static void performHomingAttack(ServerPlayer player)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            Vec3 currentPos = player.getPosition(0).add(0.0,1.0,0.0);
            Vec3 lookAngle = player.getLookAngle();

            for(int i=0;i<10;++i)
            {
                //Increment Current Position Forward
                currentPos = currentPos.add(lookAngle);
                AABB boundingBox = new AABB(currentPos.x()+3,currentPos.y()+3,currentPos.z()+3,
                        currentPos.x()-3,currentPos.y()-3,currentPos.z()-3);

                List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                        LivingEntity.class, boundingBox,
                        (enemy) -> !enemy.is(player));

                if(!nearbyEntities.isEmpty())
                {
                    LivingEntity enemy = nearbyEntities.get(0);
                    System.out.println("Targeting: "+enemy.getName().getString());
                    enemy.hurt(player.damageSources().playerAttack(player),10.0f);
                    break;
                }
            }

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
                    if(player != null){
                        performHomingAttack(player);
                    }
                });
        ctx.setPacketHandled(true);
    }
}
