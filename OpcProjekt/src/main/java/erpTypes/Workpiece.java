package erpTypes;

import java.io.Serializable;

/**
 * @author Lars Tilsner
 */
public class Workpiece implements Serializable {
    private String name;

    public Workpiece(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
