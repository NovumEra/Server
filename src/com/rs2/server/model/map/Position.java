package com.rs2.server.model.map;

import com.rs2.server.model.entity.Locatable;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 19:41.
 */
public class Position implements Locatable {

    private int x;
    private int y;
    private int z;

    public Position(int x, int y) {
        this(x, y, 0);
    }

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(int amountX, int amountY) {
        setX(getX() + amountX);
        setY(getY() + amountY);
    }

    public void set(Position position) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
    }

    public Position delta(Position b) {
        return new Position(b.getX() - getX(), b.getY() - getY());
    }

    public boolean isViewableFrom(Position other) {
        Position p = delta(other);
        return p.x <= 14 && p.x >= -15 && p.y <= 14 && p.y >= -15;
    }

    public int getLocalX() {
        return getLocalX(this);
    }

    public int getLocalX(Position base) {
        return x - 8 * base.getRegionX();
    }

    public int getRegionX() {
        return (x >> 3) - 6;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getLocalY() {
        return getLocalY(this);
    }

    public int getLocalY(Position base) {
        return y - 8 * base.getRegionY();
    }

    public int getRegionY() {
        return (y >> 3) - 6;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position p = (Position) other;
            return x == p.x && y == p.y && z == p.z;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Position(" + x + ", " + y + ", " + z + ")";
    }

    public Position randomise(int amt) {
        x += Math.ceil(Math.random() * amt);
        y += Math.ceil(Math.random() * amt);
        return this;
    }
}
