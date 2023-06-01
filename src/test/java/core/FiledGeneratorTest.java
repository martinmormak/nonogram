package core;

import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import sk.tuke.gamestudio.core.*;
import sk.tuke.gamestudio.service.FieldGeneratorJDBC;

import java.io.FileNotFoundException;

public class FiledGeneratorTest {
    @Test
    public void testReadDataFromFileAndCreateFieldIfFiledIsCorrect() throws FileNotFoundException {
        FieldGeneratorJDBC fieldGeneratorJDBC = new FieldGeneratorJDBC();
        Tile[][] expected = {
                {new ColorTile(TileState.RED), new ColorTile(TileState.GREEN), new ColorTile(TileState.BLUE)},
                {new ColorTile(TileState.YELLOW), new ColorTile(TileState.PINK), new ColorTile(TileState.CYAN)},
                {new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE)},
                {new ColorTile(TileState.BLACK), new ColorTile(TileState.WHITE), new ColorTile(TileState.WHITE)}
        };
        Field field = fieldGeneratorJDBC.generate(1);
        for (Integer r = 0; r < field.getRowCount(); r++) {
            for (Integer c = 0; c < field.getColumnCount(); c++) {
                Assertions.assertEquals(expected[r][c].getColor(), field.getCorrectField()[r][c].getColor());
            }
        }
    }
    @Test
    public void testReadDataFromFileAndCreateFieldIfRowCountIsCorrect() throws FileNotFoundException {
        FieldGeneratorJDBC fieldGeneratorJDBC = new FieldGeneratorJDBC();
        Field field = fieldGeneratorJDBC.generate(1);
        Assertions.assertEquals(4, field.getRowCount());
    }
    @Test
    public void testReadDataFromFileAndCreateFieldIfColumnCountIsCorrect() throws FileNotFoundException {
        FieldGeneratorJDBC fieldGeneratorJDBC = new FieldGeneratorJDBC();
        Field field = fieldGeneratorJDBC.generate(1);
        Assertions.assertEquals(3, field.getColumnCount());
    }
}
