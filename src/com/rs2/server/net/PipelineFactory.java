package com.rs2.server.net;

import com.rs2.server.model.Client;
import com.rs2.server.model.World;
import com.rs2.server.model.entity.player.Player;
import com.rs2.server.net.decoder.LoginDecoder;
import com.rs2.server.net.packet.Packet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:26.
 */
public class PipelineFactory extends SimpleChannelHandler implements ChannelPipelineFactory {

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

                        }
                    }
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        System.out.println(ctx.getChannel());
        System.out.println(e.getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        System.out.println(ctx.getChannel());
        System.out.println(e.getChannel());
    }

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
}
