package com.rs2.server.model.entity.player;

import com.rs2.server.model.Client;
import com.rs2.server.model.World;
import com.rs2.server.model.entity.Entity;
import com.rs2.server.model.entity.EntityHandler;
import com.rs2.server.net.StreamBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.io.IOException;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:46.
 */
public class Player extends Entity {

    private final EntityHandler<Player> localPlayers;
    private final Abilities abilities;
    private Client client;

    public Player(Client client) {
        this.localPlayers = new EntityHandler<>(World.getPlayers().getCapacity(), 255);
        this.client = client;
        this.client.setPlayer(this);
        this.abilities = new Abilities();
    }

    public EntityHandler<Player> getLocalPlayers() {
        return localPlayers;
    }

    public Abilities getAbilities() {
        return abilities;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }



    public void login() throws IOException {
        ChannelBuffer buffer = ChannelBuffers.buffer(3);
        buffer.writeByte(2);
        buffer.writeByte(0);
        buffer.writeByte(0);
        if(client != null) {
            client.send(buffer);
        }

        World.getPlayers().addEntity(this);
        sendMapRegion();
    }

    public void sendMapRegion() {
        getCurrentRegion().set(getPosition());
        setNeedsPlacement(true);

        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
        out.writeHeader(client.getCipher(), 73);
        out.writeShort(getPosition().getRegionX() + 6, StreamBuffer.ValueType.A);
        out.writeShort(getPosition().getRegionY() + 6);
        client.send(out.getBuffer());
    }
}
