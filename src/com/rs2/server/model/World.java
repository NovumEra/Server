package com.rs2.server.model;

import com.rs2.server.model.entity.EntityHandler;
import com.rs2.server.model.entity.npc.NpcUpdating;
import com.rs2.server.model.entity.player.Player;
import com.rs2.server.model.entity.player.PlayerUpdating;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:34.
 */
public class World {

    private static final Queue<Player> playerQueue = new ConcurrentLinkedQueue<Player>();
    private static EntityHandler<Player> players = new EntityHandler<>(500);

    public static void process() throws Exception {

        Player plr;
        while ((plr = playerQueue.poll()) != null) {
            try {
                plr.login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < players.getCapacity(); i++) {
            Player player = players.getEntity(i);
            if (player == null) {
                continue;
            }
            try {
                player.getClient().processQueuedPackets();
            } catch (Exception ex) {
                ex.printStackTrace();
                disconnect(player);
            }
        }

        for (int i = 0; i < players.getCapacity(); i++) {
            Player player = players.getEntity(i);
            if (player == null) {
                continue;
            }
            try {
                player.process();
            } catch (Exception ex) {
                ex.printStackTrace();
                disconnect(player);
            }
        }

        for (int i = 0; i < players.getCapacity(); i++) {
            Player player = players.getEntity(i);
            if (player == null) {
                continue;
            }
            try {
                PlayerUpdating.update(player);
                NpcUpdating.update(player);
            } catch (Exception ex) {
                ex.printStackTrace();
                disconnect(player);
            }
        }

        for (int i = 0; i < players.getCapacity(); i++) {
            Player player = players.getEntity(i);
            if (player == null) {
                continue;
            }
            try {
                player.reset();
            } catch (Exception ex) {
                ex.printStackTrace();
                disconnect(player);
            }
        }
    }

    public static void disconnect(Player player) {
        if(player != null) {
            try {
                if(player.logout()) {
                    players.remove(player.getIndex());
                    if(player.getClient() != null) {
                        player.getClient().disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void queueLogin(Player player) {
        if(!playerQueue.contains(player)) {
            playerQueue.add(player);
        }
    }

    public static EntityHandler<Player> getPlayers() {
        return players;
    }
}
