package core;

import sk.tuke.gamestudio.core.ColorTile;
import sk.tuke.gamestudio.core.TileState;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

public class ColorTileTest {
    @Test
    public void testGetColorWithParameterConstructor() {
        ColorTile colorTile = new ColorTile(TileState.BLACK);
        Assertions.assertEquals(TileState.BLACK, colorTile.getColor());
    }
    @Test
    public void testGetColorWithNoParameterConstructor() {
        ColorTile colorTile = new ColorTile();
        Assertions.assertEquals(TileState.WHITE, colorTile.getColor());
    }
}
