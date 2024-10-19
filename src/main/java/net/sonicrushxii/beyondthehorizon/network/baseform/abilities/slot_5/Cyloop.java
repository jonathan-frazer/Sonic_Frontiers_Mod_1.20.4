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
import net.sonicrushxii.beyondthehorizon.scheduler.Scheduler;

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

    public static void addToList(Deque<Vec3i> currCoords, Vec3i newPoint)
    {
        final int CYLOOP_DECAY_TIME = 30;

        if(currCoords.isEmpty())
        {
            //Add a New Point
            currCoords.addLast(newPoint);

            //Schedule it's deletion
            Scheduler.scheduleTask(() -> {
                if (!currCoords.isEmpty()) currCoords.removeFirst();
            }, CYLOOP_DECAY_TIME);
        }
        else
        {
            //Get the current Last Point
            final Vec3i lastPt = currCoords.getLast();
            System.out.println(lastPt.distSqr(newPoint));
            //If the Distance between them is small, just add
            if(lastPt.distSqr(newPoint) < 2.0)
            {
                //Add a New Point
                currCoords.addLast(newPoint);

                //Schedule it's deletion
                Scheduler.scheduleTask(() -> {
                    if (!currCoords.isEmpty()) currCoords.removeFirst();
                }, CYLOOP_DECAY_TIME);
            }
            //If the Distance between them is too Far, Interpolate
            else
            {
                //Get the two Points as Vec3's
                final Vec3 lastPoint = new Vec3(lastPt.getX(), lastPt.getY(), lastPt.getZ());
                final Vec3 endPoint = new Vec3(newPoint.getX(), newPoint.getY(), newPoint.getZ());

                //Find Direction between them
                final Vec3 dirVec = endPoint.subtract(lastPoint).normalize();

                //Loop for No. of Segments
                int noOfSegments = (int)Math.floor(lastPoint.distanceTo(endPoint));

                Vec3 interpolatedPoint = lastPoint.add(dirVec);
                while(noOfSegments-- > 0)
                {
                    interpolatedPoint = interpolatedPoint.add(dirVec);
                    //Add
                    currCoords.addLast(new Vec3i(
                            (int) Math.round(interpolatedPoint.x()),
                            (int) Math.round(interpolatedPoint.y()),
                            (int) Math.round(interpolatedPoint.z()))
                    );
                    //Schedule it's deletion
                    Scheduler.scheduleTask(() -> {
                        if (!currCoords.isEmpty()) currCoords.removeFirst();
                    }, CYLOOP_DECAY_TIME);
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
                        new ArrayDeque<Vec3i>(100)
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


