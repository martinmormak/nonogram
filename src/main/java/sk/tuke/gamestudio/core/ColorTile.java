package sk.tuke.gamestudio.core;

public class ColorTile extends Tile {
    public ColorTile() {
        super(TileState.WHITE);
    }

    public ColorTile(TileState color) {
        super(color);
    }
}
