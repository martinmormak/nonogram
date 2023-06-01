package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.core.*;

import java.sql.*;

public class FieldGeneratorJDBC implements FieldGenerator {
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    public static final String SELECT_RANDOM_FIELD_STATEMENT = "SELECT fieldsID FROM fields WHERE gamename = ? ORDER BY RANDOM() LIMIT 1";
    public static final String SELECT_FIELDS_STATEMENT = "SELECT row_count, column_count FROM fields WHERE fieldsID = ?";
    public static final String SELECT_TILES_STATEMENT = "SELECT tile_type FROM tiles WHERE fieldsID = ? ORDER BY row_number, column_number";
    Tile[][] readTileFieldDatabase(Integer fieldID, Integer rowCount, Integer columnCount) {
        if (fieldID == null || columnCount == null || rowCount == null) {return null;}
        Tile[][] tile = new ColorTile[rowCount][columnCount];
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(SELECT_TILES_STATEMENT)
        ) {
            preparedStatement.setInt(1, fieldID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int actualRow = 0;
                int actualColumn = 0;
                while (resultSet.next()) {
                    String tileType = resultSet.getString("tile_type");
                    if (tileType.toLowerCase().equals("red")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.RED);
                    } else if (tileType.toLowerCase().equals("green")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.GREEN);
                    } else if (tileType.toLowerCase().equals("blue")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.BLUE);
                    } else if (tileType.toLowerCase().equals("yellow")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.YELLOW);
                    } else if (tileType.toLowerCase().equals("pink")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.PINK);
                    } else if (tileType.toLowerCase().equals("cyan")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.CYAN);
                    } else if (tileType.toLowerCase().equals("black")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.BLACK);
                    } else if (tileType.toLowerCase().equals("white")) {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.WHITE);
                    }else {
                        tile[actualRow][actualColumn] = new ColorTile(TileState.WHITE);
                    }
                    actualColumn++;
                    if (actualColumn == columnCount) {
                        actualRow++;
                        actualColumn = 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem read field");
        }
        return tile;
    }

    Tile[][] calculateLeftHints(Tile[][] tile, Integer columnCount, Integer rowCount){
        if(tile==null||columnCount==null||rowCount==null){return null;}
        Tile[][] rowTile = new NumberTile[rowCount][columnCount];
        int sameColorCount=0;
        int nextFreeFieldPosition=0;
        for (int actualColumnCount = 0; actualColumnCount < rowCount; actualColumnCount++) {
            sameColorCount=0;
            nextFreeFieldPosition=columnCount-1;
            TileState colorBuffer=tile[actualColumnCount][columnCount-1].getColor();
            for (int j = columnCount-1; j >= 0; j--) {
                if (colorBuffer == tile[actualColumnCount][j].getColor()) {
                    sameColorCount++;
                } else {
                    if (colorBuffer != TileState.WHITE) {
                        rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                        nextFreeFieldPosition--;
                    }
                    sameColorCount = 1;
                    colorBuffer = tile[actualColumnCount][j].getColor();
                }
            }
            if(colorBuffer!= TileState.WHITE) {
                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
            }
        }
        return rowTile;
    }

    Tile[][] calculateUpperHints (Tile[][] tile, Integer columnCount, Integer rowCount){
        if(tile==null||columnCount==null||rowCount==null){return null;}
        Tile[][] columnTile = new NumberTile[columnCount][rowCount];
        int sameColorCount=0;
        int nextFreeFieldPosition=0;
        for (int actualColumnCount = 0; actualColumnCount < columnCount; actualColumnCount++) {
            sameColorCount=0;
            nextFreeFieldPosition=rowCount-1;
            TileState colorBuffer=tile[rowCount-1][actualColumnCount].getColor();
            for (int j = rowCount-1; j >= 0; j--) {
                if(colorBuffer== tile[j][actualColumnCount].getColor()){
                    sameColorCount++;
                }else {
                    if(colorBuffer!= TileState.WHITE) {
                        columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                        nextFreeFieldPosition--;
                    }
                    sameColorCount=1;
                    colorBuffer= tile[j][actualColumnCount].getColor();
                }
            }
            if(colorBuffer!= TileState.WHITE) {
                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
            }
        }
        return columnTile;
    }

    Integer pickRandomFileFromDatabase (){
        Integer ID=0;
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(SELECT_RANDOM_FIELD_STATEMENT)
        ) {
            preparedStatement.setString(1,"nonogram");
            do {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        ID=resultSet.getInt("fieldsID");
                    } else {
                        return 0;
                    }
                }
            }while (ID==1);
            return ID;
        } catch (SQLException e) {
            System.out.println("Problem selecting file");
        }
        return null;
    }

    Field readDataFromdDatabaseAndCreateField(Integer fieldID){
        if(fieldID==null){return null;}
        try (Connection connections = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connections.prepareStatement(SELECT_FIELDS_STATEMENT)
        ) {
            preparedStatement.setInt(1,fieldID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {

                    int rowCount = resultSet.getInt("row_count");
                    int columnCount = resultSet.getInt("column_count");

                    Tile[][] tile = this.readTileFieldDatabase(fieldID, rowCount, columnCount);

                    Tile[][] columnTile = calculateUpperHints(tile, columnCount, rowCount);
                    Tile[][] rowTile = calculateLeftHints(tile, columnCount, rowCount);
                    return new Field(rowCount, columnCount, rowTile, columnTile, tile);
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem read rows and count");
        }
        return null;
    }

    public Field generate() {
        return this.readDataFromdDatabaseAndCreateField(this.pickRandomFileFromDatabase());
    }

    public Field generate(Integer fieldID){
        if(fieldID==null||fieldID==0){return null;}
        try {
            return this.readDataFromdDatabaseAndCreateField(fieldID);

        }catch (Exception e){
            System.out.println("Zadal si zly subor.");
            System.exit(0);
        }
        return null;
    }
}