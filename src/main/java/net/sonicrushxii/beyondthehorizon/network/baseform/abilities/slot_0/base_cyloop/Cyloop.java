package net.sonicrushxii.beyondthehorizon.network.baseform.abilities.slot_0.base_cyloop;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.sonicrushxii.beyondthehorizon.capabilities.PlayerSonicFormProvider;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.BaseformServer;
import net.sonicrushxii.beyondthehorizon.capabilities.baseform.data.BaseformProperties;
import net.sonicrushxii.beyondthehorizon.network.PacketHandler;
import net.sonicrushxii.beyondthehorizon.network.sync.ParticleAuraPacketS2C;
import net.sonicrushxii.beyondthehorizon.network.sync.SyncPlayerFormS2C;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Deque;

public class Cyloop {
    public static final int MAX_CYLOOP_SIZE = 75;

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

    private static void deleteFromLast(Deque<Vec3> currCoords)
    {
        Vec3 deathPoint = currCoords.removeLast();
        PacketHandler.sendToALLPlayers(new ParticleAuraPacketS2C(
                new DustParticleOptions(new Vector3f(1.0f,0.0f,0.0f),10f),
                deathPoint.x(), deathPoint.y()+0.5, deathPoint.z(),
                0.0, 1f, 1f, 1f, 5, true)
        );
    }

    public static void addToList(Deque<Vec3> currCoords, Vec3 newPoint)
    {

        if(currCoords.isEmpty())
        {
            //Add a New Point
            currCoords.addFirst(newPoint);
        }
        else
        {
            //Get the current Last Point
            final Vec3 lastPt = currCoords.getFirst();
            //If the Distance between them is small, just add
            if(lastPt.distanceToSqr(newPoint) < 2.0)
            {
                //Add a New Point
                if(currCoords.size() == MAX_CYLOOP_SIZE) deleteFromLast(currCoords);
                currCoords.addFirst(newPoint);
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
                    //Add
                    if(currCoords.size() == MAX_CYLOOP_SIZE) deleteFromLast(currCoords);
                    currCoords.addFirst(interpolatedPoint);

                    //Move to next Point
                    interpolatedPoint = interpolatedPoint.add(dirVec);
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
                        new ArrayDeque<Vec3>(50)
                );
            }

            //If Deactivating, print List of Traversal. Discard the old list
            if(!activate)
            {
                CyloopMath.cyloopEffect(player,
                        BaseformServer.cyloopCoords.get(player.getUUID()).toArray());
            }

            PacketHandler.sendToALLPlayers(
                    new SyncPlayerFormS2C(
                            player.getId(),
                            playerSonicForm
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


