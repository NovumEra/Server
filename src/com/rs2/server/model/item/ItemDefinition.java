package com.rs2.server.model.item;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 13:20.
 */
public class ItemDefinition {

    private int id;
    private String name;
    private String examine;
    private boolean noted;
    private boolean canBeNoted;
    private boolean canBeStacked;
    private int parentId;
    private int notedId;
    private boolean members;
    private int shopValue;
    private int highAlchValue;
    private int lowAlchValue;
    private int[] bonuses;
    private double weight;
    private int slot;
    private boolean fullMask;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public boolean isNoted() {
        return noted;
    }

    public void setNoted(boolean noted) {
        this.noted = noted;
    }

    public boolean isCanBeNoted() {
        return canBeNoted;
    }

    public void setCanBeNoted(boolean canBeNoted) {
        this.canBeNoted = canBeNoted;
    }

    public boolean isCanBeStacked() {
        return canBeStacked;
    }

    public void setCanBeStacked(boolean canBeStacked) {
        this.canBeStacked = canBeStacked;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getNotedId() {
        return notedId;
    }

    public void setNotedId(int notedId) {
        this.notedId = notedId;
    }

    public boolean isMembers() {
        return members;
    }

    public void setMembers(boolean members) {
        this.members = members;
    }

    public int getShopValue() {
        return shopValue;
    }

    public void setShopValue(int shopValue) {
        this.shopValue = shopValue;
    }

    public int getHighAlchValue() {
        return highAlchValue;
    }

    public void setHighAlchValue(int highAlchValue) {
        this.highAlchValue = highAlchValue;
    }

    public int getLowAlchValue() {
        return lowAlchValue;
    }

    public void setLowAlchValue(int lowAlchValue) {
        this.lowAlchValue = lowAlchValue;
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean isFullMask() {
        return fullMask;
    }

    public void setFullMask(boolean fullMask) {
        this.fullMask = fullMask;
    }

    public static ItemDefinition forId(int id) {
        if(id < 0 || id > definitions.length)
            return new ItemDefinition();
        if(definitions[id] == null) {
            definitions[id] = new ItemDefinition();
            return definitions[id];
        }
        return definitions[id];
    }

    private static ItemDefinition[] definitions = new ItemDefinition[12000];
}
