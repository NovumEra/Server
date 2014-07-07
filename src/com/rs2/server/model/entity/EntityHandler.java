package com.rs2.server.model.entity;

import java.util.Iterator;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 14:35.
 */
public class EntityHandler<T extends Entity> implements Iterable<T> {

    private final Entity[] entities;
    private final int capacity;
    private int entityCount;

    public EntityHandler(int capacity) {
        this(capacity, capacity);
    }

    public EntityHandler(int size, int capacity) {
        this.entities = new Entity[size];
        this.capacity = capacity;
        this.entityCount = 0;
    }

    public T getEntity(int index) {
        return (T) entities[index];
    }

    public int addEntity(int index, T entity) {
        synchronized (entities) {
            if(index == -1) {
                index = getFreeIndex();
                entity.setIndex(index);
            }
            if(index != -1 && !contains(index)) {
                entities[index] = entity;
                entityCount++;
                return index;
            }
        }
        return -1;
    }

    public int addEntity(T entity) {
        return addEntity(entity.getIndex(), entity);
    }

    public boolean contains(int index) {
        synchronized (entities) {
            return entities[index] != null;
        }
    }

    public void remove(int index) {
        synchronized (entities) {
            entities[index] = null;
            entityCount--;
        }
    }

    public boolean hasFreeSlot() {
        return entityCount < capacity;
    }

    private int getFreeIndex() {
        for(int i = 0; i < capacity; i++) {
            if(entities[i] == null)
                return i;
        }
        return -1;
    }

    public int getEntityCount() {
        return entityCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public Entity[] getEntities() {
        return entities;
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < capacity && entities[currentIndex] != null;
            }

            @Override
            public T next() {
                return (T) entities[currentIndex++];
            }

            @Override
            public void remove() {
                // TODO Auto-generated method stub
            }
        };
        return it;
    }
}
