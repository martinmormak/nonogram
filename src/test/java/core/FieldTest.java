package core;

import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import sk.tuke.gamestudio.core.*;

public class FieldTest {
    @Test
    public void testCompareFieldsIfIsGameFinished() {
        Tile[][] correctField = new Tile[][]{
                {new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE), new ColorTile(TileState.BLACK)},
                {new ColorTile(TileState.WHITE), new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE)},
                {new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE), new ColorTile(TileState.BLACK)}
        };

        Field field = new Field(3, 3, null, null, correctField);

        field.updateActualField(TileState.BLACK, 1, 1);
        field.updateActualField(TileState.WHITE, 1, 2);
        field.updateActualField(TileState.BLACK, 1, 3);
        field.updateActualField(TileState.WHITE, 2, 1);
        field.updateActualField(TileState.BLACK, 2, 2);
        field.updateActualField(TileState.WHITE, 2, 3);
        field.updateActualField(TileState.BLACK, 3, 1);
        field.updateActualField(TileState.WHITE, 3, 2);
        field.updateActualField(TileState.BLACK, 3, 3);

        Assertions.assertEquals(FieldState.SOLVED, field.getFieldState());
    }
    @Test
    public void testCompareFieldsIfIsNotGameFinished() {
        Tile[][] correctField = new Tile[][]{
                {new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE), new ColorTile(TileState.BLACK)},
                {new ColorTile(TileState.WHITE), new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE)},
                {new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE), new ColorTile(TileState.BLACK)}
        };

        Field field = new Field(3, 3, null, null, correctField);

        field.updateActualField(TileState.BLACK, 1, 1);
        field.updateActualField(TileState.WHITE, 1, 2);
        field.updateActualField(TileState.BLACK, 1, 3);
        field.updateActualField(TileState.WHITE, 2, 1);
        field.updateActualField(TileState.BLACK, 2, 2);
        field.updateActualField(TileState.WHITE, 2, 3);
        field.updateActualField(TileState.BLACK, 3, 1);
        field.updateActualField(TileState.WHITE, 3, 2);
        field.updateActualField(TileState.WHITE, 3, 3);

        Assertions.assertEquals(FieldState.PLAYED, field.getFieldState());
    }
    @Test
    public void testUpdateActualFieldWithCorrectCoordinates() {

        Field field = new Field(4, 3, null, null, null);

        field.updateActualField(TileState.BLACK, 2, 4);

        Assertions.assertEquals(TileState.BLACK, field.getActualFieldTile(3, 1).getColor());

        field.updateActualField(TileState.BLACK, 2, 1);

        Assertions.assertEquals(TileState.BLACK, field.getActualFieldTile(0, 1).getColor());

        field.updateActualField(TileState.BLACK, 3, 2);

        Assertions.assertEquals(TileState.BLACK, field.getActualFieldTile(1, 2).getColor());

        field.updateActualField(TileState.BLACK, 1, 2);

        Assertions.assertEquals(TileState.BLACK, field.getActualFieldTile(1, 0).getColor());
    }
    @Test
    public void testUpdateActualFieldWithWrongXCoordinates() {
        Field field = new Field(2, 3, null, null, null);

        field.updateActualField(TileState.BLACK, 0, 2);
        for (Integer actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                Assertions.assertSame(TileState.WHITE, field.getActualFieldTile(actualRow, actualColumn).getColor());
            }
        }

        field.updateActualField(TileState.BLACK, 4, 2);
        for (Integer actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                Assertions.assertSame(TileState.WHITE, field.getActualFieldTile(actualRow, actualColumn).getColor());
            }
        }
    }
    @Test
    public void testUpdateActualFieldWithWrongYCoordinates() {
        Field field = new Field(2, 3, null, null, null);

        field.updateActualField(TileState.BLACK, 1, 0);
        for (Integer actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                Assertions.assertSame(TileState.WHITE, field.getActualFieldTile(actualRow, actualColumn).getColor());
            }
        }

        field.updateActualField(TileState.BLACK, 1, 3);
        for (Integer actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                Assertions.assertSame(TileState.WHITE, field.getActualFieldTile(actualRow, actualColumn).getColor());
            }
        }
    }
    @Test
    public void testGetActualFieldWithWrongCoordinates() {
        Tile[][] expected = new Tile[][]{
                {new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE)},
                {new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE)},
                {new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE)}
        };

        Field field = new Field(2, 3, null, null, null);

        field.updateActualField(TileState.BLACK, 1, 0);
        for (Integer actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                Assertions.assertSame(expected[actualRow][actualColumn].getColor(), field.getActualFieldTile(actualRow, actualColumn).getColor());
            }
        }

        field.updateActualField(TileState.BLACK, 1, 3);
        for (Integer actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                Assertions.assertSame(expected[actualRow][actualColumn].getColor(), field.getActualFieldTile(actualRow, actualColumn).getColor());
            }
        }
    }
}
