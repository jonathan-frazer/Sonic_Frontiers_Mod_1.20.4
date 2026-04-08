package net.sonicrushxii.beyondthehorizon.modded;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.sonicrushxii.beyondthehorizon.BeyondTheHorizon;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicForm;

import java.util.function.Supplier;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BeyondTheHorizon.MOD_ID);

    public static final Supplier<AttachmentType<PlayerSonicForm>> PLAYER_SONIC_FORM =
            ATTACHMENT_TYPES.register("player_sonic_form", () ->
                    AttachmentType.serializable(PlayerSonicForm::new).build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
