package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5;

import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.ArrayList;

public class CyloopTrigger {

    private final boolean activate;

    public CyloopTrigger(boolean activate) {
        this.activate = activate;
    }

    public CyloopTrigger(FriendlyByteBuf buffer){
        this.activate = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.activate);
    }

    public static void isLoop(ArrayList<Vec3i> cyloopPath)
    {

    }

    public static void toggleCyloop(ServerPlayer player, boolean activate)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Activate Cyloop
            baseformProperties.cylooping = activate;

            //If Activating, Initialize out list
            if(activate)
            {
                BaseformServer.cyloopCoords.put(player.getUUID(),new ArrayList<Vec3i>());
            }

            //If Deactivating, print List of Traversal
            if(!activate)
            {
                System.out.print("[");
                for(Vec3i point : BaseformServer.cyloopCoords.getOrDefault(player.getUUID(),new ArrayList<Vec3i>()))
                    System.out.print(point+",");
                System.out.print("]");

                BaseformServer.cyloopCoords.put(player.getUUID(),new ArrayList<Vec3i>());
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
                    if(player != null)
                    {
                        toggleCyloop(player,activate);
                    }
                });
        ctx.setPacketHandled(true);
    }
}


