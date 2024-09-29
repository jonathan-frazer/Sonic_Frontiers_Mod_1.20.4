package net.sonicrushxii.beyondthehorizon.event_handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

public class LoginHandler {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if(player == null) return;
        if(!player.level().isClientSide()) onServerLogin((ServerPlayer) player);
    }

    private void onServerLogin(ServerPlayer player)
    {
        System.out.println("Logged In");
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm -> {
            PacketHandler.sendToPlayer(player, new SyncPlayerFormS2C(
                    playerSonicForm.getCurrentForm(),
                    playerSonicForm.getFormProperties()
            ));
        });
    }
}
