package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Random;
import java.util.Scanner;

public class FieldGeneratorTXT implements FieldGenerator{
    Tile[][] readTileField(Scanner scanner, Integer rowCount, Integer columnCount){
        if(scanner==null||columnCount==null||rowCount==null){return null;}
        Tile[][] tile = new ColorTile[rowCount][columnCount];
        for (int actualRow = 0; actualRow < rowCount; actualRow++) {
            String[] line = scanner.nextLine().trim().split(" ");
            for (int actialColumn = 0; actialColumn < columnCount; actialColumn++) {
                char[] colors=line[actialColumn].toCharArray();
                for (char color : colors) {
                    switch (color) {
                        case 'r':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.RED);
                            break;
                        case 'g':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.GREEN);
                            break;
                        case 'b':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.BLUE);
                            break;
                        case 'y':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.YELLOW);
                            break;
                        case 'p':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.PINK);
                            break;
                        case 'c':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.CYAN);
                            break;
                        case 'l':
                            tile[actualRow][actialColumn] = new ColorTile(TileState.BLACK);
                            break;
                        default:
                            tile[actualRow][actialColumn] = new ColorTile(TileState.WHITE);
                            break;
                    }
                }
            }
        }
        return tile;
    }

    Tile[][] calculateLeftHints(Tile[][] tile, Integer columnCount, Integer rowCount){
        if(tile==null||columnCount==null||rowCount==null){return null;}
        Tile[][] rowTile = new NumberTile[rowCount][columnCount];
        Integer sameColorCount=0;
        Integer nextFreeFieldPosition=0;
        for (int actualColumnCount = 0; actualColumnCount < rowCount; actualColumnCount++) {
            sameColorCount=0;
            nextFreeFieldPosition=columnCount-1;
            TileState colorBuffer=tile[actualColumnCount][columnCount-1].getColor();
            for (int j = columnCount-1; j >= 0; j--) {
                switch (tile[actualColumnCount][j].getColor()) {
                    case RED:
                        if (colorBuffer == TileState.RED) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.RED;
                        }
                        break;
                    case GREEN:
                        if (colorBuffer == TileState.GREEN) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.GREEN;
                        }
                        break;
                    case BLUE:
                        if (colorBuffer == TileState.BLUE) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.BLUE;
                        }
                        break;
                    case YELLOW:
                        if (colorBuffer == TileState.YELLOW) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.YELLOW;
                        }
                        break;
                    case PINK:
                        if (colorBuffer == TileState.PINK) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.PINK;
                        }
                        break;
                    case CYAN:
                        if (colorBuffer == TileState.CYAN) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.CYAN;
                        }
                        break;
                    case BLACK:
                        if (colorBuffer == TileState.BLACK) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.BLACK;
                        }
                        break;
                    case WHITE:
                        if (colorBuffer == TileState.WHITE) {
                            sameColorCount++;
                        } else {
                            if (colorBuffer != TileState.WHITE) {
                                rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount = 1;
                            colorBuffer = TileState.WHITE;
                        }
                        break;
                    default:
                        rowTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                        sameColorCount = 0;
                        colorBuffer = TileState.WHITE;
                        break;
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
        Integer sameColorCount=0;
        Integer nextFreeFieldPosition=0;
        for (int actualColumnCount = 0; actualColumnCount < columnCount; actualColumnCount++) {
            sameColorCount=0;
            nextFreeFieldPosition=rowCount-1;
            TileState colorBuffer=tile[rowCount-1][actualColumnCount].getColor();
            for (int j = rowCount-1; j >= 0; j--) {
                switch (tile[j][actualColumnCount].getColor()) {
                    case RED:
                        if(colorBuffer== TileState.RED){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.RED;
                        }
                        break;
                    case GREEN:
                        if(colorBuffer== TileState.GREEN){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.GREEN;
                        }
                        break;
                    case BLUE:
                        if(colorBuffer== TileState.BLUE){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.BLUE;
                        }
                        break;
                    case YELLOW:
                        if(colorBuffer== TileState.YELLOW){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.YELLOW;
                        }
                        break;
                    case PINK:
                        if(colorBuffer== TileState.PINK){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.PINK;
                        }
                        break;
                    case CYAN:
                        if(colorBuffer== TileState.CYAN){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.CYAN;
                        }
                        break;
                    case BLACK:
                        if(colorBuffer== TileState.BLACK){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.BLACK;
                        }
                        break;
                    case WHITE:
                        if(colorBuffer== TileState.WHITE){
                            sameColorCount++;
                        }else {
                            if(colorBuffer!= TileState.WHITE) {
                                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                                nextFreeFieldPosition--;
                            }
                            sameColorCount=1;
                            colorBuffer= TileState.WHITE;
                        }
                        break;
                    default:
                        columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
                        sameColorCount=0;
                        colorBuffer= TileState.WHITE;
                        break;
                }
            }
            if(colorBuffer!= TileState.WHITE) {
                columnTile[actualColumnCount][nextFreeFieldPosition] = new NumberTile(colorBuffer, sameColorCount);
            }
        }
        return columnTile;
    }

    File pickRandomFile (){
        File dir = new File(".");

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });

        Random rand = new Random();
        File randomFile = files[rand.nextInt(files.length)];
        return randomFile;
    }

    Field readDataFromFileAndCreateField(File file) throws FileNotFoundException {
        if(file==null){return null;}
        Scanner scanner = new Scanner(file);

        int rowCount=0;
        int columnCount=0;
        String[] hintStrings = scanner.nextLine().trim().split(" ");
        rowCount = Integer.parseInt(hintStrings[0]);
        columnCount = Integer.parseInt(hintStrings[1]);

        Tile[][] tile = this.readTileField(scanner,rowCount,columnCount);
        scanner.close();

        Tile[][] columnTile = calculateUpperHints(tile,columnCount,rowCount);
        Tile[][] rowTile = calculateLeftHints(tile,columnCount,rowCount);
        return new Field(rowCount,columnCount,rowTile,columnTile,tile);
    }
    @Override
    public Field generate() throws FileNotFoundException {
        return this.readDataFromFileAndCreateField(this.pickRandomFile());
    }

    public Field generate(String fieldName) throws FileNotFoundException {
        if(fieldName==null){return null;}
        try {
            return this.readDataFromFileAndCreateField(new File(fieldName));

        }catch (FileNotFoundException e){
            System.out.println("Zadal si zly subor.");
            System.exit(0);
        }
        return this.readDataFromFileAndCreateField(this.pickRandomFile());
    }
}
