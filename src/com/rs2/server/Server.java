package com.rs2.server;

/**
 * Created by Cory Nelson
 *     on 04/07/14.
 *     at 14:06.
 */
public class Server {

    private static Server ourInstance;

    public static void main(String[] args) {
        Server.ourInstance = new Server();
    }

    private Server() {

    }

    public static Server getInstance() {
        return ourInstance;
    }
}
