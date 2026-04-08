package net.sonicrushxii.beyondthehorizon.capabilities;

import net.minecraft.world.entity.Entity;
import net.sonicrushxii.beyondthehorizon.modded.ModAttachments;

/**
 * Utility class for accessing PlayerSonicForm data attachments.
 * Replaces the old Forge capability provider pattern.
 */
public class PlayerSonicFormProvider {

    /**
     * Gets the PlayerSonicForm data attached to the given entity.
     * In NeoForge, data attachments are always present (no Optional/LazyOptional).
     */
    public static PlayerSonicForm getPlayerSonicForm(Entity entity) {
        return entity.getData(ModAttachments.PLAYER_SONIC_FORM);
    }
}
