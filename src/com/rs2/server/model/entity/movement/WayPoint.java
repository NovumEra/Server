package com.rs2.server.model.entity.movement;

import com.rs2.server.model.map.Position;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 15:33.
 */
public class WayPoint extends Position {

    private int direction;

    public WayPoint(int x, int y, int direction) {
        super(x, y);
        setDirection(direction);
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
