package com.rs2.server;

/**
 * Created by Cory Nelson
 *     on 04/07/14.
 *     at 14:06.
 */
public class Server implements Runnable {

    private static Server ourInstance;

    public static void main(String[] args) {
        Server.ourInstance = new Server();
        new Thread(Server.ourInstance).start();
    }

    private Server() {

    }

    @Override
    public void run() {

    }

    public static Server getInstance() {
        return ourInstance;
    }
}
