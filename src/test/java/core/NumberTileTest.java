package core;

import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import sk.tuke.gamestudio.core.NumberTile;
import sk.tuke.gamestudio.core.TileState;

public class NumberTileTest {
    @Test
    public void testConstructorWithOneParameter() {
        NumberTile numberTile = new NumberTile(10);
        Assertions.assertEquals(TileState.WHITE, numberTile.getColor());
        Assertions.assertEquals(10, numberTile.getValue());
    }
    @Test
    public void testConstructorWithTwoParameter() {
        NumberTile numberTile = new NumberTile(TileState.BLACK, 1);
        Assertions.assertEquals(TileState.BLACK, numberTile.getColor());
        Assertions.assertEquals(1, numberTile.getValue());
    }
}
