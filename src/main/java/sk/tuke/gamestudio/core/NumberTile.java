package sk.tuke.gamestudio.core;

public class NumberTile extends Tile {
    private Integer value;

    public NumberTile(Integer value) {
        this(TileState.WHITE, value);
    }

    public NumberTile(TileState color, Integer value) {
        super(color);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
