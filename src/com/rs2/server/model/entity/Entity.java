package com.rs2.server.model.entity;

import com.rs2.server.model.item.Item;
import com.rs2.server.model.map.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:35.
 */
public abstract class Entity {

    protected final Map<String, Object> attributes = new HashMap<String, Object>();

    protected final Position position = new Position(3145, 3225).randomise(3);
    protected final Position currentRegion = new Position(0, 0, 0);
    protected final UpdateFlags updateFlags = new UpdateFlags();

    private final int[] appearance = new int[7];
    private final int[] colors = new int[5];
    private Item[] equipment = new Item[12];

    protected boolean updateRequired;
    protected boolean needsPlacement;

    protected int primaryDirection;
    protected int secondaryDirection;
    protected int index = -1;
    private boolean resetMovementQueue;

    public abstract void resetAppearance();

    public abstract void process();

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        if(!attributes.containsKey(key))
            return null;
        return (T) attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position.set(position);
    }

    public Position getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentRegion(Position position) {
        this.currentRegion.set(position);
    }

    public UpdateFlags getUpdateFlags() {
        return updateFlags;
    }

    public boolean isUpdateRequired() {
        return updateRequired;
    }

    public void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }

    public void setNeedsPlacement(boolean needsPlacement) {
        this.needsPlacement = needsPlacement;
    }

    public boolean isNeedsPlacement() {
        return needsPlacement;
    }

    public int getPrimaryDirection() {
        return primaryDirection;
    }

    public void setPrimaryDirection(int primaryDirection) {
        this.primaryDirection = primaryDirection;
    }

    public int getSecondaryDirection() {
        return secondaryDirection;
    }

    public void setSecondaryDirection(int secondaryDirection) {
        this.secondaryDirection = secondaryDirection;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isResetMovementQueue() {
        return resetMovementQueue;
    }

    public void setResetMovementQueue(boolean resetMovementQueue) {
        this.resetMovementQueue = resetMovementQueue;
    }

    public int[] getAppearance() {
        return appearance;
    }

    public int[] getColors() {
        return colors;
    }

    public Item[] getEquipment() {
        return equipment;
    }
}
