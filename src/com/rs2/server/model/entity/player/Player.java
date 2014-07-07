package com.rs2.server.model.entity.player;

import com.rs2.server.model.Client;
import com.rs2.server.model.World;
import com.rs2.server.model.entity.Entity;
import com.rs2.server.model.entity.EntityHandler;
import com.rs2.server.net.StreamBuffer;
import com.rs2.server.util.Util;
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

        // Set the default appearance.
        getAppearance()[Util.APPEARANCE_SLOT_CHEST] = 18;
        getAppearance()[Util.APPEARANCE_SLOT_ARMS] = 26;
        getAppearance()[Util.APPEARANCE_SLOT_LEGS] = 36;
        getAppearance()[Util.APPEARANCE_SLOT_HEAD] = 0;
        getAppearance()[Util.APPEARANCE_SLOT_HANDS] = 33;
        getAppearance()[Util.APPEARANCE_SLOT_FEET] = 42;
        getAppearance()[Util.APPEARANCE_SLOT_BEARD] = 10;

        // Set the default colors.
        getColors()[0] = 7;
        getColors()[1] = 8;
        getColors()[2] = 9;
        getColors()[3] = 5;
        getColors()[4] = 0;
    }

    @Override
    public void process() {

    }

    public void sendMapRegion() {
        getCurrentRegion().set(getPosition());
        setNeedsPlacement(true);

        if(client != null && client.isWritable()) {
            StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
            out.writeHeader(client.getCipher(), 73);
            out.writeShort(getPosition().getRegionX() + 6, StreamBuffer.ValueType.A);
            out.writeShort(getPosition().getRegionY() + 6);
            client.send(out.getBuffer());
        }
    }

    public void login() throws IOException {
        if(client != null && client.isWritable()) {
            ChannelBuffer buffer = ChannelBuffers.buffer(3);
            buffer.writeByte(2);
            buffer.writeByte(0);
            buffer.writeByte(0);
            client.send(buffer);
        }

        World.getPlayers().addEntity(this);
        sendMapRegion();
        setUpdateRequired(true);
        getUpdateFlags().addUpdate(Util.PLAYER_APPEARANCE_UPDATE_MASK);
    }

    @Override
    public void resetAppearance() {
        setPrimaryDirection(-1);
        setSecondaryDirection(-1);
        setUpdateRequired(false);
        getUpdateFlags().reset();
        setResetMovementQueue(false);
        setNeedsPlacement(false);
    }

    public boolean logout() {
        return true;
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
}
