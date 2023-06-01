package sk.tuke.gamestudio.core;

import java.io.Serializable;

public class Field implements Serializable {
    private final int rowCount;
    private final int columnCount;
    private FieldState fieldState;
    private final Tile[][] rowField;
    private final Tile[][] columnField;
    private final Tile[][] correctField;
    private Tile[][] actualField;
    private Long startTime;

    public Field(int rowCount, int columnCount, Tile[][] rowField, Tile[][] columnField, Tile[][] correctField) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.fieldState = FieldState.PLAYED;
        this.rowField = rowField;
        this.columnField = columnField;
        this.correctField = correctField;
        this.actualField = new Tile[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                this.actualField[row][column] = new ColorTile(TileState.WHITE);
            }
        }
        this.startTime = System.currentTimeMillis();
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public FieldState getFieldState() {
        return fieldState;
    }

    public void setFieldState(FieldState fieldState) {
        this.fieldState = fieldState;
    }

    public Tile[][] getColumnField() {
        return columnField;
    }

    public Tile[][] getRowField() {
        return rowField;
    }

    public Tile getActualFieldTile(Integer actualRow, Integer actualColumn) {
        if(actualColumn<0||actualColumn>=this.getColumnCount()||actualRow<0||actualRow>=this.getRowCount()) {
            return null;
        }
        return actualField[actualRow][actualColumn];
    }

    public Tile[][] getCorrectField() {
        return correctField;
    }

    public boolean compareFields() {
        if (actualField == null || correctField == null) {
            return false;
        }
        for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
            for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                if (this.actualField[actualRow][actualColumn].getColor() != this.correctField[actualRow][actualColumn].getColor()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void updateActualField(TileState tileState, Integer userInputXPosition, Integer userInputYPosition) {
        if (userInputXPosition <= 0 || userInputXPosition > this.getColumnCount() || userInputYPosition <= 0 || userInputYPosition > this.getRowCount() || this.actualField[userInputYPosition - 1][userInputXPosition - 1] == null || actualField == null) {
            return;
        }
        this.actualField[userInputYPosition - 1][userInputXPosition - 1].setColor(tileState);
        if (this.compareFields() == true) {
            this.setFieldState(FieldState.SOLVED);
        }
    }

    public int colorCountInField(){
        TileState usedColors[] = new TileState[TileState.values().length];
        int colorsCount = 0;
        for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
            for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                boolean isInList = false;
                for (int index = 0; index < colorsCount; index++) {
                    if (usedColors[index] == this.correctField[actualRow][actualColumn].getColor()) {
                        isInList = true;
                    }
                }
                if (!isInList) {
                    usedColors[colorsCount] = this.correctField[actualRow][actualColumn].getColor();
                    colorsCount++;
                }
            }
        }
        return columnCount;
    }
    void automaticFillActualFieldRows(int colorsCount){
        for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
            TileState previousColor = TileState.WHITE;
            int count = 0;
            for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                if (this.correctField[actualRow][actualColumn].getColor() == TileState.WHITE && previousColor != TileState.WHITE && actualColumn != this.getColumnCount() - 1 && colorsCount <= 2 || this.correctField[actualRow][actualColumn].getColor() != TileState.WHITE) {
                    count++;
                }
                previousColor = this.correctField[actualRow][actualColumn].getColor();
            }
            if (count == this.getColumnCount()) {
                for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                    updateActualField(this.correctField[actualRow][actualColumn].getColor(), actualColumn + 1, actualRow + 1);
                }
            }
        }
    }
    void automaticFillActualFieldColumns(int colorsCount){
        for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
            TileState previousColor = TileState.WHITE;
            int count = 0;
            for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
                if (this.correctField[actualRow][actualColumn].getColor() == TileState.WHITE && previousColor != TileState.WHITE && actualRow != this.getRowCount() - 1 && colorsCount <= 2 || this.correctField[actualRow][actualColumn].getColor() != TileState.WHITE) {
                    count++;
                }
                previousColor = this.correctField[actualRow][actualColumn].getColor();
            }
            if (count == this.getRowCount()) {
                for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
                    updateActualField(this.correctField[actualRow][actualColumn].getColor(), actualColumn + 1, actualRow + 1);
                }
            }
        }
    }

    public void automaticFillActualField() {
        /*TileState usedColors[] = new TileState[TileState.values().length];
        int colorsCount = 0;
        for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
            for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                boolean isInList = false;
                for (int index = 0; index < colorsCount; index++) {
                    if (usedColors[index] == this.correctField[actualRow][actualColumn].getColor()) {
                        isInList = true;
                    }
                }
                if (!isInList) {
                    usedColors[colorsCount] = this.correctField[actualRow][actualColumn].getColor();
                    colorsCount++;
                }
            }
        }*/
        int colorsCount = colorCountInField();
        /*for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
            TileState previousColor = TileState.WHITE;
            int count = 0;
            for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                if (this.correctField[actualRow][actualColumn].getColor() == TileState.WHITE && previousColor != TileState.WHITE && actualColumn != this.getColumnCount() - 1 && colorsCount <= 2 || this.correctField[actualRow][actualColumn].getColor() != TileState.WHITE) {
                    count++;
                }
                previousColor = this.correctField[actualRow][actualColumn].getColor();
            }
            if (count == this.getColumnCount()) {
                for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
                    updateActualField(this.correctField[actualRow][actualColumn].getColor(), actualColumn + 1, actualRow + 1);
                }
            }
        }*/
        this.automaticFillActualFieldRows(colorsCount);
        /*for (int actualColumn = 0; actualColumn < this.getColumnCount(); actualColumn++) {
            TileState previousColor = TileState.WHITE;
            int count = 0;
            for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
                if (this.correctField[actualRow][actualColumn].getColor() == TileState.WHITE && previousColor != TileState.WHITE && actualRow != this.getRowCount() - 1 && colorsCount <= 2 || this.correctField[actualRow][actualColumn].getColor() != TileState.WHITE) {
                    count++;
                }
                previousColor = this.correctField[actualRow][actualColumn].getColor();
            }
            if (count == this.getRowCount()) {
                for (int actualRow = 0; actualRow < this.getRowCount(); actualRow++) {
                    updateActualField(this.correctField[actualRow][actualColumn].getColor(), actualColumn + 1, actualRow + 1);
                }
            }
        }*/
        this.automaticFillActualFieldColumns(colorsCount);
    }

    public Integer countingScore() {
        return this.getRowCount() * this.getColumnCount() * 10 - (int) (System.currentTimeMillis() - startTime) / 1000;
    }
}
