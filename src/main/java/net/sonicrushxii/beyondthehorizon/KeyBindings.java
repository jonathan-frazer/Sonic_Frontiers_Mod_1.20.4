package net.sonicrushxii.beyondthehorizon;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private KeyBindings() {}

    private static final String CATEGORY = "key.categories."+BeyondTheHorizon.MOD_ID;

    public final KeyMapping doubleJump = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".DoubleJump",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_SPACE,-1),
            CATEGORY
    );
    public final KeyMapping toggleDangerSense = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".ToggleDangerSense",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_P,-1),
            CATEGORY
    );
    public final KeyMapping parryKey = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".ParryKey",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_GRAVE,-1),
            CATEGORY
    );
    public final KeyMapping virtualSlotUse = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_R,-1),
            CATEGORY
    );

    public final KeyMapping useAbility1 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_1",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_Z,-1),
            CATEGORY
    );
    public final KeyMapping useAbility2 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_2",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_X,-1),
            CATEGORY
    );
    public final KeyMapping useAbility3 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_3",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_C,-1),
            CATEGORY
    );
    public final KeyMapping useAbility4 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_4",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V,-1),
            CATEGORY
    );
    public final KeyMapping useAbility5 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_5",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_G,-1),
            CATEGORY
    );
    public final KeyMapping useAbility6 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_6",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B,-1),
            CATEGORY
    );
    public final KeyMapping useSingleAbility = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_Single",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            InputConstants.MOUSE_BUTTON_RIGHT,
            CATEGORY
    );
    public final KeyMapping useUltimateAbility = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersUltUse",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_U,-1),
            CATEGORY
    );
    public final KeyMapping helpButton = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersHelp",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_H,-1),
            CATEGORY
    );
}
