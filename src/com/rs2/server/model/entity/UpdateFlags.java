package com.rs2.server.model.entity;

import com.rs2.server.model.map.Position;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 12:58.
 */
public class UpdateFlags {

    private int updateMask = 0x0;

    private int graphicsId;
    private int graphicsDelay;
    private int animationId;
    private int animationDelay;
    private String forceChatMessage = "";
    private int entityFaceIndex;
    private Position face = new Position(0,0);
    private int damage;
    private int hitType;
    private int damage2;
    private int hitType2;
    private byte[] chatText = new byte[0];
    private int chatColor;
    private int chatEffects;

    //MASK ALTERATION METHODS

    public void addUpdate(int updateFlag) {
        this.updateMask |= updateFlag;
    }

    public void removeUpdate(int updateFlag) {
        this.updateMask &= ~updateFlag;
    }

    public boolean needsUpdating(int updateFlag) {
        return (updateMask & updateFlag) == updateFlag;
    }

    public void reset() {
        this.updateMask = 0x0;
    }

    // GETTERS AND SETTERS

    public int getUpdateMask() {
        return updateMask;
    }

    public int getGraphicsId() {
        return graphicsId;
    }

    public void setGraphicsId(int graphicsId) {
        this.graphicsId = graphicsId;
    }

    public int getGraphicsDelay() {
        return graphicsDelay;
    }

    public void setGraphicsDelay(int graphicsDelay) {
        this.graphicsDelay = graphicsDelay;
    }

    public int getAnimationId() {
        return animationId;
    }

    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public int getAnimationDelay() {
        return animationDelay;
    }

    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
    }

    public String getForceChatMessage() {
        return forceChatMessage;
    }

    public void setForceChatMessage(String forceChatMessage) {
        this.forceChatMessage = forceChatMessage;
    }

    public int getEntityFaceIndex() {
        return entityFaceIndex;
    }

    public void setEntityFaceIndex(int entityFaceIndex) {
        this.entityFaceIndex = entityFaceIndex;
    }

    public Position getFace() {
        return face;
    }

    public void setFace(Position face) {
        this.face = face;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHitType() {
        return hitType;
    }

    public void setHitType(int hitType) {
        this.hitType = hitType;
    }

    public int getDamage2() {
        return damage2;
    }

    public void setDamage2(int damage2) {
        this.damage2 = damage2;
    }

    public int getHitType2() {
        return hitType2;
    }

    public void setHitType2(int hitType2) {
        this.hitType2 = hitType2;
    }

    public byte[] getChatText() {
        return chatText;
    }

    public void setChatText(byte[] chatText) {
        this.chatText = chatText;
    }

    public int getChatColor() {
        return chatColor;
    }

    public void setChatColor(int chatColor) {
        this.chatColor = chatColor;
    }

    public int getChatEffects() {
        return chatEffects;
    }

    public void setChatEffects(int chatEffects) {
        this.chatEffects = chatEffects;
    }
}
