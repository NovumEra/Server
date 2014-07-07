package com.rs2.server.net;

import com.rs2.server.model.Client;
import com.rs2.server.model.World;
import com.rs2.server.model.entity.player.Player;
import com.rs2.server.net.decoder.LoginDecoder;
import com.rs2.server.net.packet.Packet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.io.IOException;
import java.security.SecureRandom;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:26.
 */
public class PipelineFactory extends SimpleChannelHandler implements ChannelPipelineFactory {

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        DefaultChannelPipeline channelPipeline = new DefaultChannelPipeline();
        channelPipeline.addLast("encoder", new OneToOneEncoder() {
            protected ChannelBuffer encode(ChannelHandlerContext chc, Channel chan, Object in) throws Exception {
                return (ChannelBuffer) in;
            }
        });
        channelPipeline.addLast("decoder", new LoginDecoder());
        channelPipeline.addLast("handler", this);
        return channelPipeline;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getChannel().isConnected()) {
            Client client = (Client) e.getChannel().getAttachment();
            if(client != null) {
                Player player = client.getPlayer();
                if(player != null) {
                    if(e.getMessage() instanceof Player) {
                        player = (Player) e.getMessage();
                        World.queueLogin(player);
                    }
                    if(e.getMessage() instanceof Packet) {
                        if(player != null) {
                            player.getClient().queuePacket((Packet) e.getMessage());
                        } else {
                            client.disconnect();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        disconnect(ctx);

        try {
            throw e.getCause();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        disconnect(ctx);
    }

    private void disconnect(ChannelHandlerContext ctx) {
        try {
            if(ctx.getChannel().getAttachment() != null) {
                Player player = ((Client) ctx.getChannel().getAttachment()).getPlayer();
                if(player != null) {
                    World.disconnect(player);
                }
            }
        } catch (Exception ignored){}
    }
}
