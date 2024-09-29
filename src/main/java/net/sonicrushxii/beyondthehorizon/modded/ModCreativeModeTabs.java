package net.sonicrushxii.beyondthehorizon.modded;


import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BeyondTheHorizon.MOD_ID);


    public static final RegistryObject<CreativeModeTab> SONIC_TAB = CREATIVE_MODE_TABS.register("sonic_tab",
            () -> CreativeModeTab.builder().icon(() -> {
                        ItemStack sonicHead = new ItemStack(Items.PLAYER_HEAD);
                        CompoundTag nbt = new CompoundTag();

                        // SkullOwner tag
                        CompoundTag skullOwner = new CompoundTag();
                        CompoundTag properties = new CompoundTag();
                        ListTag textures = new ListTag();
                        CompoundTag texture = new CompoundTag();
                        texture.putString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBjN2NlZWNjODliNTY0MjNhOWU4YWFiMTE3NjRkZTI5MDIyNjU4MzA5YTUyNjY2M2JmMzQyNGY0N2NhZDlmOCJ9fX0=");
                        textures.add(texture);
                        properties.put("textures", textures);
                        skullOwner.put("Properties", properties);
                        skullOwner.putIntArray("Id", new int[] {512370214, -95272899, -2003262887, 1067375885});
                        nbt.put("SkullOwner", skullOwner);

                        sonicHead.setTag(nbt);

                        return sonicHead;
                    })
                    .title(Component.translatable("creativetab.sonic_tab"))
                    .displayItems(((pParameters, pOutput) -> {
                        //Sonic Head
                        {
                            ItemStack customPlayerHead = new ItemStack(Items.PLAYER_HEAD);
                            CompoundTag nbt = new CompoundTag();

                            // Custom NBT data
                            nbt.putByte("BeyondTheHorizon", (byte) 2);

                            // SkullOwner tag
                            CompoundTag skullOwner = new CompoundTag();
                            CompoundTag properties = new CompoundTag();
                            ListTag textures = new ListTag();
                            CompoundTag texture = new CompoundTag();
                            texture.putString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBjN2NlZWNjODliNTY0MjNhOWU4YWFiMTE3NjRkZTI5MDIyNjU4MzA5YTUyNjY2M2JmMzQyNGY0N2NhZDlmOCJ9fX0=");
                            textures.add(texture);
                            properties.put("textures", textures);
                            skullOwner.put("Properties", properties);
                            skullOwner.putIntArray("Id", new int[]{512370214, -95272899, -2003262887, 1067375885});
                            nbt.put("SkullOwner", skullOwner);

                            // Display tag
                            CompoundTag display = new CompoundTag();
                            ListTag lore = new ListTag();
                            lore.add(StringTag.valueOf("{\"text\":\"Adapted from Sonic Frontiers\",\"color\": \"light_purple\"}"));
                            display.put("Lore", lore);
                            display.putString("Name", "{\"text\":\"Sonic Head\",\"color\": \"blue\",\"italic\": false}");
                            nbt.put("display", display);

                            customPlayerHead.setTag(nbt);

                            pOutput.accept(customPlayerHead);
                        }
                        pOutput.accept(ModItems.BASEFORM_LIGHTSPEED_CHESTPLATE.get());
                        pOutput.accept(ModItems.BASEFORM_LIGHTSPEED_LEGGINGS.get());
                        pOutput.accept(ModItems.BASEFORM_LIGHTSPEED_BOOTS.get());

                    }))
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
