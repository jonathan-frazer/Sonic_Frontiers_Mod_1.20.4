package net.sonicrushxii.beyondthehorizon;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

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
            InputConstants.getKey(InputConstants.KEY_LALT,-1),
            CATEGORY
    );
    public final KeyMapping cycleSlotKey = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".CycleSlot",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            InputConstants.MOUSE_BUTTON_MIDDLE,
            CATEGORY
    );
    public final KeyMapping meleeSlotKey = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".MeleeSlot",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            3,
            CATEGORY
    );
    public final KeyMapping rangedSlotKey = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".RangedSlot",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            4,
            CATEGORY
    );
    public final KeyMapping dashKey = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".Dash",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_G,-1),
            CATEGORY
    );

    public final KeyMapping useAbility1 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_1",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_R,-1),
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
            InputConstants.getKey(InputConstants.KEY_V,-1),
            CATEGORY
    );
    public final KeyMapping useAbility4 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_4",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_C,-1),
            CATEGORY
    );
    public final KeyMapping useAbility5 = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersSlot_5",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_F,-1),
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
            InputConstants.getKey(InputConstants.KEY_H,-1),
            CATEGORY
    );
    public final KeyMapping helpButton = new KeyMapping(
            "key."+ BeyondTheHorizon.MOD_ID+".FrontiersHelp",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_F4,-1),
            CATEGORY
    );
}
