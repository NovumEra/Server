package com.rs2.server.model.entity;

import com.rs2.server.model.map.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:35.
 */
public class Entity {

    protected final Map<String, Object> attributes = new HashMap<String, Object>();

    protected final Position position = new Position(3145, 3225).randomise(3);
    protected final Position currentRegion = new Position(0, 0, 0);
    protected boolean needsPlacement;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position.set(position);
    }

    public Position getCurrentRegion() {
        return currentRegion;
    }

    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        if(!attributes.containsKey(key))
            return null;
        return (T) attributes.get(key);
    }

    public void setNeedsPlacement(boolean needsPlacement) {
        this.needsPlacement = needsPlacement;
    }

    public boolean isNeedsPlacement() {
        return needsPlacement;
    }
}
