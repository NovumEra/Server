package com.rs2.server.model.item;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 13:20.
 */
public class Item {

    private int id, amount;
    private ItemDefinition definition;

    public Item(int id) {
        this(id, 1);
    }

    public Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int incrementAmount() {
        this.amount = (int) Math.min(Integer.MAX_VALUE, (long) this.amount + 1);
        return (int) Math.max(0, ((long) this.amount + 1) - Integer.MAX_VALUE);
    }

    public void decrementAmount() {
        this.amount = (int) Math.max(1, (long) this.amount - 1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.definition = ItemDefinition.forId(id);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemDefinition getDefinition() {
        return definition;
    }
}
