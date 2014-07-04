package com.rs2.server;

import com.rs2.server.model.World;
import com.rs2.server.net.PipelineFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * Created by Cory Nelson
 *     on 04/07/14.
 *     at 14:06.
 */
public class Server implements Runnable {

    private final static Properties properties = new Properties();

    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream("./server.properties")) {
            properties.load(in);
        } catch (IOException e) {
            try (FileOutputStream out = new FileOutputStream("./server.properties")) {
                properties.put("ServerName", "RSPS-Server");
                properties.store(out, null);
            } catch (IOException ignored) {}
        }

        new Thread(new Server()).start();
    }

    @Override
    public void run() {

        System.out.println("Starting '"+get("ServerName")+"'...");
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 43594);

        System.out.println("Binding to '" + address + "'.");
        System.out.println("_____________________________________");
        System.out.println();

        Channel channel = getBootStrap().bind(address);
        while (channel.isOpen()) {
            long cycleTime = cycle() / 1000000L;
            if(cycleTime < 600) {
                System.out.println("players: " + World.getPlayers().getEntityCount() + ", cycle time:" + cycleTime);
                try {
                    Thread.sleep((600L - cycleTime));
                } catch (Exception ignored){}
            }
        }
    }

    private ServerBootstrap getBootStrap() {
        final ExecutorService boss = newCachedThreadPool(), worker = newCachedThreadPool();
        final NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(boss, worker);
        final ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(new PipelineFactory());
        return bootstrap;
    }

    private long cycle() {
        long startTime = System.nanoTime();
        try {
            World.process();
        } catch (Exception ignored){}
        return (System.nanoTime() - startTime);
    }

    public static String get(String name) {
        return String.valueOf(properties.get(name));
    }
}
