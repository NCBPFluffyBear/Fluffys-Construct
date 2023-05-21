package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.data.Offset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Maps the offsets for smeltery bricks
 * based on the controller location
 */
public enum SmelteryFootprint {

    THREE_BY_THREE(
            new ArrayList<>(Arrays.asList(
                    new Offset(1, 0, -1), new Offset(1, -1, -1), new Offset(1, -2, -1),
                    new Offset(2, 0, -1), new Offset(2, -1, -1), new Offset(2, -2, -1),
                    new Offset(3, 0, -1), new Offset(3, -1, -1), new Offset(3, -2, -1)
            )),

            new ArrayList<>(Arrays.asList(
                    new Offset(0, 0, 0), new Offset(0, -1, 0), new Offset(0, -2, 0), // Front wall
                    new Offset(1, 1, 0), new Offset(2, 1, 0), new Offset(3, 1, 0), // Right wall
                    new Offset(1, -3, 0), new Offset(2, -3, 0), new Offset(3, -3, 0), // Left wall
                    new Offset(4, -2, 0), new Offset(4, -1, 0), new Offset(4, 0, 0) // Rear wall
            ))
    );

    private final List<Offset> base;
    private final List<Offset> wall;

    SmelteryFootprint(List<Offset> base, List<Offset> wall) {
        this.base = base;
        this.wall = wall;
    }

    public List<Offset> getBase() {
        return base;
    }

    public List<Offset> getWall() {
        return wall;
    }
}
