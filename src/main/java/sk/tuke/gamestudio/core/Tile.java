package sk.tuke.gamestudio.core;

import java.io.Serializable;

public abstract class Tile implements Serializable {
    private TileState color;

    public Tile(TileState color) {
        this.color = color;
    }

    public void setColor(TileState color) {
        this.color = color;
    }

    public TileState getColor() {
        return color;
    }
}
