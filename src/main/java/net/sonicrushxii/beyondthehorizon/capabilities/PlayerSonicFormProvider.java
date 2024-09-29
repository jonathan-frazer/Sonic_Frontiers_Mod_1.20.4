package net.sonicrushxii.beyondthehorizon.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSonicFormProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerSonicForm> PLAYER_SONIC_FORM = CapabilityManager.get(new CapabilityToken<PlayerSonicForm>() {});

    private PlayerSonicForm playerSonicForm = null;
    private final LazyOptional<PlayerSonicForm> optional = LazyOptional.of(this::createPlayerSonicForm);

    private PlayerSonicForm createPlayerSonicForm() {
        if(this.playerSonicForm == null)
            this.playerSonicForm = new PlayerSonicForm();

        return this.playerSonicForm;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if(capability == PLAYER_SONIC_FORM)
        {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSonicForm().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSonicForm().loadNBTData(nbt);
    }
}
