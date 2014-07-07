package com.rs2.server.model;

import com.rs2.server.model.entity.player.Player;
import com.rs2.server.net.packet.Packet;
import com.rs2.server.net.packet.PacketHandlers;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 16:35.
 */
public class Client {

    private final Queue<Packet> queuedPackets;
    private final Channel channel;

    private SecureRandom cipher;
    private String username;
    private String password;
    private Player player;
    private int userId;

    public Client(Channel channel) {
        this.queuedPackets = new ConcurrentLinkedQueue<>();
        this.channel = channel;
    }

    public void queuePacket(Packet packet) {
        queuedPackets.add(packet);
    }

    public void processQueuedPackets() throws IOException {
        Packet packet;
        while((packet = queuedPackets.poll()) != null) {
            PacketHandlers.handlePacket(player, packet);
        }
    }

    public void send(ChannelBuffer buffer) {
        if(channel == null || !channel.isConnected()) {
            return;
        }
        if(channel.isWritable()) {
            channel.write(buffer);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public SecureRandom getCipher() {
        return cipher;
    }

    public void setCipher(SecureRandom cipher) {
        this.cipher = cipher;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isWritable() {
        return channel != null && channel.isWritable();
    }

    public String getHost() {
        if(channel == null) {
            return "unknown";
        }
        return ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public String toString() {
        return getUsername() == null ? "Client(" + getHost() + ")" : "Player(" + getUsername() + ":" + getPassword() + " - " + getHost() + ")";
    }

    public void disconnect() {
        if(getChannel() != null) {
            getChannel().close();
            getChannel().setAttachment(null);
        }
    }
}
