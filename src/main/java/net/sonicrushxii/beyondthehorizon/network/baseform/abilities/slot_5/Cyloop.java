package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_5;

import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Cyloop {

    private final boolean activate;

    public Cyloop(boolean activate) {
        this.activate = activate;
    }

    public Cyloop(FriendlyByteBuf buffer){
        this.activate = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.activate);
    }

    public static void isLoop(ArrayList<Vec3i> cyloopPath)
    {

    }

    public static void addToList(Deque<Vec3> currCoords, Vec3 newPoint)
    {
        final int CYLOOP_DECAY_TIME = 30;

        while(currCoords.size() >= 49)
            currCoords.removeFirst();

        if(currCoords.isEmpty())
        {
            //Add a New Point
            currCoords.addLast(newPoint);
        }
        else
        {
            //Get the current Last Point
            final Vec3 lastPt = currCoords.getLast();
            //If the Distance between them is small, just add
            if(lastPt.distanceToSqr(newPoint) < 2.0)
            {
                //Add a New Point
                currCoords.addLast(newPoint);
            }
            //If the Distance between them is too Far, Interpolate
            else
            {
                //Find Direction between them
                final Vec3 dirVec = newPoint.subtract(lastPt).normalize();

                //Loop for No. of Segments
                int noOfSegments = (int)Math.floor(lastPt.distanceTo(newPoint));

                Vec3 interpolatedPoint = lastPt.add(dirVec);
                while(noOfSegments-- > 0)
                {
                    interpolatedPoint = interpolatedPoint.add(dirVec);
                    //Add
                    currCoords.addLast(interpolatedPoint);
                }
            }
        }
    }

    public static void toggleCyloop(ServerPlayer player, boolean activate)
    {
        player.getCapability(PlayerSonicFormProvider.PLAYER_SONIC_FORM).ifPresent(playerSonicForm-> {
            BaseformProperties baseformProperties = (BaseformProperties) playerSonicForm.getFormProperties();

            //Activate Cyloop
            baseformProperties.cylooping = activate;

            //If Activating, Initialize our list
            if(activate)
            {
                BaseformServer.cyloopCoords.put(
                        player.getUUID(),
                        new ArrayDeque<>(50)
                );
            }

            //If Deactivating, print List of Traversal. Discard the old list
            /*if(!activate)
            {}*/

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


