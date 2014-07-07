package com.rs2.server.net.decoder;

import com.rs2.server.model.Client;
import com.rs2.server.model.World;
import com.rs2.server.model.entity.player.Player;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 16:20.
 */
public class LoginDecoder extends FrameDecoder {

    private static final BigInteger RSA_MODULUS = new BigInteger("118254211712390939185286648537626990438692309885668101018362589365089701544129890816813488775741575738784357128583215579705271074058917199186849834952324221225643616487195902610488808005882862369019991081749954217006620792631407040715373884438947494566753016771492063434885040082231984689834669607178222727729");
    private static final BigInteger RSA_EXPONENT = new BigInteger("32053158015455584535261486253908568566655937757434855829381769648922798697375885079724046181733575711792808948107973229746317886988763677409023917300045578520590278031874812039845865007662877668630575597489034263864685191939639640896945027760959810260489441559101759987268562485914956861796383760016276542721");

    private Stage stage = Stage.INITIALISE_CONNECTION;
    private int loginEncryptSize;
    private long serverKey;
    private int clientUID;

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (channel.isConnected()) {
            switch (stage) {
                case INITIALISE_CONNECTION:
                    if (buffer.readableBytes() < 1)
                        return null;

                    int loginRequest = buffer.readUnsignedByte();
                    if(loginRequest != 14) {
                        System.out.println("Invalid Login Request: "+loginRequest);
                        channel.close();
                        return null;
                    }

                    ChannelBuffer response = ChannelBuffers.buffer(17);
                    response.writeByte(World.getPlayers().hasFreeSlot() ? 0 : 7);
                    response.writeLong(serverKey = new SecureRandom().nextLong());
                    channel.write(response);

                    stage = Stage.READ_CONNECTION_HEADER;
                    break;

                case READ_CONNECTION_HEADER:
                    if (buffer.readableBytes() < 2)
                        return null;

                    int loginType = buffer.readUnsignedByte();
                    if(loginType != 16 && loginType != 18) {
                        System.out.println("Invalid Login type: "+loginType);
                        channel.close();
                        return null;
                    }

                    loginEncryptSize = buffer.readUnsignedByte();
                    if (loginEncryptSize <= 0) {
                        System.out.println("Invalid Login Encrypt Size: "+loginEncryptSize);
                        channel.close();
                        return null;
                    }

                    stage = Stage.READ_CONNECTION_PAYLOAD;
                    break;

                case READ_CONNECTION_PAYLOAD:

                    if (buffer.readableBytes() < loginEncryptSize)
                        return null;

                    loginEncryptSize--;
                    int reportedSize = buffer.readByte() & 0xFF;
                    if(reportedSize != loginEncryptSize) {
                        channel.close();
                        return false;
                    }

                    byte[] encryptionBytes = new byte[loginEncryptSize];
                    buffer.readBytes(encryptionBytes);
                    ByteBuffer rsaBuffer = ByteBuffer.wrap(new BigInteger(encryptionBytes).modPow(RSA_EXPONENT, RSA_MODULUS).toByteArray());

                    int rsaOpCode = rsaBuffer.get() & 0xFF;
                    if (rsaOpCode != 10) {
                        channel.close();
                        return null;
                    }

                    long clientKey = rsaBuffer.getLong();
                    long reportedServerKey = rsaBuffer.getLong();

                    if (reportedServerKey != serverKey) {
                        channel.close();
                        return null;
                    }

                    byte[] sessionKey = ByteBuffer.allocate(16).putLong(clientKey).putLong(serverKey).array();
                    SecureRandom inCipher = new SecureRandom(sessionKey);
                    for (int i = 0; i < sessionKey.length; i++)
                        sessionKey[i] += 50;
                    SecureRandom outCipher = new SecureRandom(sessionKey);

                    clientUID = rsaBuffer.getInt();

                    StringBuilder username = new StringBuilder(), password = new StringBuilder();

                    byte usernameChar;
                    while ((usernameChar = rsaBuffer.get()) != ((byte) '\n')) {
                        username.append((char) usernameChar);
                    }

                    byte passwordChar;
                    while ((passwordChar = rsaBuffer.get()) != ((byte) '\n')) {
                        password.append((char) passwordChar);
                    }

                    channel.getPipeline().remove("decoder");
                    channel.getPipeline().addFirst("decoder", new PacketDecoder(inCipher));

                    Client client = new Client(channel);
                    client.setUsername(username.toString());
                    client.setPassword(password.toString());
                    client.setUserId(clientUID);
                    client.setCipher(outCipher);

                    channel.setAttachment(client);
                    return new Player(client);
            }
        }
        return null;
    }

    public enum Stage {
        INITIALISE_CONNECTION, READ_CONNECTION_HEADER, READ_CONNECTION_PAYLOAD, LOGGED_IN
    }
}
