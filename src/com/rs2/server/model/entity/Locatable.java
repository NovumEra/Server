package com.rs2.server.model.entity;

import com.rs2.server.model.map.Position;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 19:41.
 */
public interface Locatable {
    Position getPosition();
}