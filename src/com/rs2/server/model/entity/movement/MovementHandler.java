package com.rs2.server.model.entity.movement;

import com.rs2.server.model.entity.Entity;
import com.rs2.server.model.entity.player.Player;
import com.rs2.server.model.map.Position;
import com.rs2.server.util.Util;

import java.awt.*;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 15:33.
 */
public class MovementHandler {

    private Deque<WayPoint> wayPoints = new LinkedList<WayPoint>();

    private final Entity entity;
    private boolean runToggled;
    private boolean runPath;

    public MovementHandler(Entity entity) {
        this.entity = entity;
    }

    public void process() {
        WayPoint walkPoint = null;
        WayPoint runPoint = null;

        // Handle the movement.
        walkPoint = wayPoints.poll();
        if (isRunToggled() || isRunPath()) {
            runPoint = wayPoints.poll();
        }
        if (walkPoint != null && walkPoint.getDirection() != -1) {
            entity.getPosition().move(Util.DIRECTION_DELTA_X[walkPoint.getDirection()], Util.DIRECTION_DELTA_Y[walkPoint.getDirection()]);
            entity.setPrimaryDirection(walkPoint.getDirection());
        }
        if (runPoint != null && runPoint.getDirection() != -1) {
            entity.getPosition().move(Util.DIRECTION_DELTA_X[runPoint.getDirection()], Util.DIRECTION_DELTA_Y[runPoint.getDirection()]);
            entity.setSecondaryDirection(runPoint.getDirection());
        }

        // Check for region changes.
        int deltaX = entity.getPosition().getX() - entity.getCurrentRegion().getRegionX() * 8;
        int deltaY = entity.getPosition().getY() - entity.getCurrentRegion().getRegionY() * 8;
        if (deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY > 88) {
            if (entity instanceof Player) {
                ((Player)entity).sendMapRegion();
            }
        }
    }

    public void reset() {
        setRunPath(false);
        wayPoints.clear();

        // Set the base point as this position.
        Position p = entity.getPosition();
        wayPoints.add(new WayPoint(p.getX(), p.getY(), -1));
    }

    public void addToPath(Position position) {
        if (wayPoints.size() == 0) {
            reset();
        }
        WayPoint last = wayPoints.peekLast();
        int deltaX = position.getX() - last.getX();
        int deltaY = position.getY() - last.getY();
        int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        for (int i = 0; i < max; i++) {
            if (deltaX < 0) {
                deltaX++;
            } else if (deltaX > 0) {
                deltaX--;
            }
            if (deltaY < 0) {
                deltaY++;
            } else if (deltaY > 0) {
                deltaY--;
            }
            addStep(position.getX() - deltaX, position.getY() - deltaY);
        }
    }
    private void addStep(int x, int y) {
        if (wayPoints.size() >= 100) {
            return;
        }
        WayPoint last = wayPoints.peekLast();
        int deltaX = x - last.getX();
        int deltaY = y - last.getY();
        int direction = Util.direction(deltaX, deltaY);
        if (direction > -1) {
            wayPoints.add(new WayPoint(x, y, direction));
        }
    }

    public void finish() {
        wayPoints.removeFirst();
    }

    public boolean isRunToggled() {
        return runToggled;
    }

    public void setRunToggled(boolean runToggled) {
        this.runToggled = runToggled;
    }

    public boolean isRunPath() {
        return runPath;
    }

    public void setRunPath(boolean runPath) {
        this.runPath = runPath;
    }
}
