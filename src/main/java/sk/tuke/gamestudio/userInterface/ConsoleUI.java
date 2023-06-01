package sk.tuke.gamestudio.userInterface;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.core.FieldState;
import sk.tuke.gamestudio.core.NumberTile;
import sk.tuke.gamestudio.core.TileState;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Data;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI implements UserInterface {
    private Field field;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private DataService dataService;
    @Autowired
    private FieldGenerator fieldGenerator;
    private String playersName;
    int indexBufer;

    public ConsoleUI(Field field,String playersName) {
        this.field = field;
        this.playersName=playersName;
        this.indexBufer=-1;
        /*this.scoreService = new ScoreServiceJDBC();
        this.commentService= new CommentServiceJDBC();
        this.ratingService = new RatingServiceJDBC();*/
    }

    public Field getField() {
        return field;
    }

    public String getPlayersName() {
        return playersName;
    }
    TileState convertCharToTileState(char colorChar) throws Exception {
        TileState tileState = TileState.WHITE;
        switch (colorChar) {
            case 'w':
                tileState = TileState.WHITE;
                break;
            case 'r':
                tileState = TileState.RED;
                break;
            case 'g':
                tileState = TileState.GREEN;
                break;
            case 'b':
                tileState = TileState.BLUE;
                break;
            case 'y':
                tileState = TileState.YELLOW;
                break;
            case 'p':
                tileState = TileState.PINK;
                break;
            case 'c':
                tileState = TileState.CYAN;
                break;
            case 'l':
                tileState = TileState.BLACK;
                break;
            default:
                System.out.println("Your color value is wrong. Color must be one of this (w (white),r (red),g (green),b (blue),l (black),y (yellow), c (cyan),p (purple))");
                throw new Exception("");
        }
        return tileState;
    }

    void endGame() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\u001B[31m" + "Exit menu\n" + "\u001B[0m" + "If you really want leave game enter yes else enter no");
        while (true) {
            String input = scanner.nextLine().trim();
            if ("yes".equals(input.toLowerCase())) {
                while (true) {
                    System.out.println("Enter if you want save game enter yes else enter no");
                    input = scanner.nextLine().trim();
                    if ("yes".equals(input.toLowerCase())) {
                        this.printSavedGames();
                        System.out.println("Enter on what slot would you like save this game");
                        input = scanner.nextLine().trim();
                        try {
                            this.saveGame(Integer.parseInt(input));
                            if(indexBufer!=Integer.parseInt(input)) {
                                this.deleteSave(indexBufer);
                            }
                        }catch (Exception e){
                            System.out.println("Sorry i have problem with save your game!");
                        }
                        this.getField().setFieldState(FieldState.FAILED);
                        System.out.println("Sorry you exit before solve!");
                        return;
                    } else if ("no".equals(input.toLowerCase())) {
                        this.getField().setFieldState(FieldState.FAILED);
                        System.out.println("Sorry you exit before solve!");
                        return;
                    } else {
                        System.out.println("Your input is wrong");
                    }
                }
            } else if ("no".equals(input.toLowerCase())) {
                return;
            } else {
                System.out.println("Your input is wrong");
            }
        }
    }

    String handleInput(Scanner scanner) {
        if (scanner == null) return null;
        Matcher matcher;
        String inputString;
        Boolean isFirst=true;
        do {
            if(isFirst==false){
                System.out.println("Your input is wrong");
            }
            System.out.println("Enter your turn in this format color (w (white),r (red),g (green),b (blue),l (black),y (yellow),c (cyan),p (purple)) xPosition <1-" + this.getField().getColumnCount() + "> yPosition <1-" + this.getField().getRowCount() + ">");
            inputString = scanner.nextLine();
            Pattern pattern = Pattern.compile("^(Exit|End|Help|Print Ratings|Print Comments|Add Rating|Add Comment|[w|r|g|b|l|y|c|p]\\s\\d+\\s\\d+)$", Pattern.CASE_INSENSITIVE);//"^Exit$|^End$|^Help$|^[w|r|g|b|l|y|c|p]\\s\\d+\\s\\d+$"
            matcher = pattern.matcher(inputString);
            isFirst=false;
        } while (!matcher.find());
        return inputString;
    }
    private void loadSavegame(Scanner scanner) throws IOException {System.out.println("Enter yes if you want load before saved game and no if you don't");
        while (true) {
            String input = scanner.nextLine().trim();
            if ("yes".equals(input.toLowerCase())) {
                this.printSavedGames();
                System.out.println("Enter what save would you like load");
                input = scanner.nextLine().trim();
                try {
                    this.getSavedGameData(Integer.parseInt(input));
                    this.indexBufer=Integer.parseInt(input);
                }catch (Exception e){
                    field=fieldGenerator.generate();
                }
                break;
            } else if ("no".equals(input.toLowerCase())) {
                field=fieldGenerator.generate();
                System.out.println("Enter yes if you want fill full rows and columns and no if you don't");
                while (true) {
                    input = scanner.nextLine().trim();
                    if ("yes".equals(input.toLowerCase())) {
                        getField().automaticFillActualField();
                        break;
                    } else if ("no".equals(input.toLowerCase())) {
                        break;
                    } else {
                        System.out.println("Your input is wrong");
                    }
                }
                break;
            } else {
                System.out.println("Your input is wrong");
            }
        }
    }

    public void play() throws IOException {
        Scanner scanner = new Scanner(System.in);
        this.loadSavegame(scanner);
        this.printTopScores();
        this.show();
        field.setStartTime();
        //System.out.println(field.countingScore());
        while (this.getField().getFieldState() == FieldState.PLAYED) {
            String line = handleInput(scanner);
            if ("end".equals(line.toLowerCase()) || "exit".equals(line.toLowerCase())) {
                this.endGame();
            } else if ("help".equals(line.toLowerCase())) {
                System.out.print("\u001B[31m" + "Help menu\n" + "\u001B[0m" + "Help - show help menu\nEnd or Exit - to Exit menu\nAdd comment - to add comment\nPrint comments - to show comments\nAdd rating - to add rating\nPrint ratings - to show ratings\nTo fill tile - ");
            } else if ("print ratings".equals(line.toLowerCase())) {
                this.printRatings();
            } else if ("print comments".equals(line.toLowerCase())) {
                this.printComments();
            } else if ("add rating".equals(line.toLowerCase())) {
                this.addRating(scanner);
            } else if ("add comment".equals(line.toLowerCase())) {
                this.addComment(scanner);
            } else {
                try {
                    String input[] = line.split(" ");
                    TileState tileState = convertCharToTileState(input[0].charAt(0));
                    Integer x = Integer.parseInt(input[1]);
                    Integer y = Integer.parseInt(input[2]);
                    if (x > 0 && x <= this.getField().getColumnCount()) {
                        if (y > 0 && y <= this.getField().getRowCount()) {
                            field.updateActualField(tileState, x, y);
                            this.show();
                        } else {
                            System.out.println("Your y value is wrong. Y must be in <1:" + this.getField().getRowCount() + ">");
                        }
                    } else {
                        System.out.println("Your x value is wrong. X must be in <1:" + this.getField().getColumnCount() + ">");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Your input is to short.");
                } catch (Exception e) {
                }
            }
        }
        if (this.getField().getFieldState() == FieldState.SOLVED) {
            System.out.println("Congratulation you won!");
            try {
                scoreService.addScore(new Score("nonogram", this.getPlayersName(), this.getField().countingScore(), new Date()));
                if(indexBufer!=-1) {
                    this.deleteSave(indexBufer);
                }
            }catch (ScoreException e){
                System.out.println("I have problem with add your score to database");
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }
        scanner.close();
    }

    Integer countMaxNotEmptyRowsInColumnField() {
        Integer maxColumnField = 0;
        for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
            Integer maxActualNotEmptyRows = 0;
            for (Integer actualRow = 0; actualRow < this.getField().getRowCount(); actualRow++) {
                if (this.getField().getColumnField()[actualColumn][actualRow] != null && ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() != 0) {//if columnField[actualColumn][actualRow]!=null
                    maxActualNotEmptyRows++;
                }
            }
            if (maxActualNotEmptyRows >= maxColumnField) {
                maxColumnField = maxActualNotEmptyRows;
            }
        }
        return maxColumnField;
    }

    Integer countMaxNotEmptyRowsInRowField() {
        Integer maxRowField = 0;
        for (Integer actualRow = 0; actualRow < this.getField().getRowCount(); actualRow++) {
            Integer maxActualNotEmptyRows = 0;
            for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                if (this.getField().getRowField()[actualRow][actualColumn] != null && ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() != 0) {//if rowField[actualColumn][actualRow]!=null
                    maxActualNotEmptyRows++;
                }
            }
            if (maxActualNotEmptyRows >= maxRowField) {
                maxRowField = maxActualNotEmptyRows;
            }
        }
        return maxRowField;
    }

    void printUpperHints(Integer maxColumnField, Integer maxRowField) {
        if (maxColumnField == null || maxRowField == null) {
            return;
        }
        for (Integer actualRow = this.getField().getRowCount() - maxColumnField; actualRow < this.getField().getRowCount(); actualRow++) {
            for (Integer actualColumn = this.getField().getColumnCount() - maxRowField; actualColumn < this.getField().getColumnCount() + 1; actualColumn++) {
                System.out.print("\u001B[0m" + "   ");
            }
            for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                if (this.getField().getColumnField()[actualColumn][actualRow] != null && ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() != 0) {//if columnField[actualColumn][actualRow]!=null
                    switch (this.getField().getColumnField()[actualColumn][actualRow].getColor()) {
                        case RED:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[41m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[41m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case GREEN:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[42m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[42m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case BLUE:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[44m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[44m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case YELLOW:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[43m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[43m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case PINK:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[45m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[45m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case CYAN:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[46m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[46m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case BLACK:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[40m" + "\u001B[37m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[40m" + "\u001B[37m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        case WHITE:
                            if (((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() < 10) {
                                System.out.print("\u001B[47m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[47m" + "\u001B[30m" + ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString() + " ");
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.print("\u001B[0m" + "   ");
                }
            }
            System.out.println("\u001B[0m");
        }
        System.out.println("\u001B[0m");
    }

    void printFieldWithLeftHints(Integer maxRowField) {
        if (maxRowField == null) {
            return;
        }
        for (Integer actualRow = 0; actualRow < this.getField().getRowCount(); actualRow++) {
            for (Integer actualColumn = this.getField().getColumnCount() - maxRowField; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                //try {
                if (this.getField().getRowField()[actualRow][actualColumn] != null && ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() != 0) {//if rowField[actualColumn][actualRow]!=null
                    switch (this.getField().getRowField()[actualRow][actualColumn].getColor()) {
                        case RED:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[41m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[41m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case GREEN:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[42m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[42m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case BLUE:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[44m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[44m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case YELLOW:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[43m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[43m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case PINK:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[45m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[45m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case CYAN:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[46m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[46m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case BLACK:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[40m" + "\u001B[37m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[40m" + "\u001B[37m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        case WHITE:
                            if (((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() < 10) {
                                System.out.print("\u001B[47m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + "  ");
                            } else {
                                System.out.print("\u001B[47m" + "\u001B[30m" + ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString() + " ");
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.print("\u001B[0m" + "   ");
                }
                /*}catch (NullPointerException e){
                    System.out.print("\u001B[0m"+"   ");
                }*/
            }
            System.out.print("\u001B[0m" + "   ");
            for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.WHITE) {
                    System.out.print("\u001B[47m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.RED) {
                    System.out.print("\u001B[41m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.GREEN) {
                    System.out.print("\u001B[42m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.BLUE) {
                    System.out.print("\u001B[44m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.YELLOW) {
                    System.out.print("\u001B[43m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.PINK) {
                    System.out.print("\u001B[45m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.CYAN) {
                    System.out.print("\u001B[46m" + "   ");
                } else if (this.getField().getActualFieldTile(actualRow, actualColumn).getColor() == TileState.BLACK) {
                    System.out.print("\u001B[40m" + "   ");
                }
            }

            /*System.out.print("\u001B[0m" + "          ");

            for(Integer actualColumn=0;actualColumn<this.getField().getColumnCount();actualColumn++){
                if(this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.WHITE){
                    System.out.print("\u001B[47m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.RED){
                    System.out.print("\u001B[41m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.GREEN){
                    System.out.print("\u001B[42m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.BLUE){
                    System.out.print("\u001B[44m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.YELLOW){
                    System.out.print("\u001B[43m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.PINK){
                    System.out.print("\u001B[45m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.CYAN){
                    System.out.print("\u001B[46m"+"   ");
                } else if (this.getField().getCorrectField()[actualRow][actualColumn].getColor()== TileState.BLACK){
                    System.out.print("\u001B[40m"+"   ");
                }
            }*/
            System.out.println("\u001B[0m");
        }
        System.out.println("\u001B[0m");
    }

    void show() {//printActualFieldWithHints
        for (int index = 0; index < 3; index++) {
            System.out.println();
        }
        Integer maxColumnField = this.countMaxNotEmptyRowsInColumnField();
        Integer maxRowField = this.countMaxNotEmptyRowsInRowField();

        printUpperHints(maxColumnField, maxRowField);
        printFieldWithLeftHints(maxRowField);
    }

    void printTopScores() {
        List<Score> scores=null;
        try {
            System.out.println("Top Scores:");
            scores = scoreService.getTopScores("nonogram");
        }catch (ScoreException e){
            System.out.println("\nI have problem with print top scores:\n");
        }catch (Exception e){
            System.out.println(e.toString());
        }
        int i = 1;
        if (scores!=null) {
            for (Score score : scores) {
                System.out.println(i + " " + score.getPlayer() + ": " + score.getPoints() + ", " + score.getPlayedOn());
                i++;
            }
            System.out.println("-----------------------------------");
        }else {
            System.out.println("Problem to load score");
        }
    }
    void printComments() {
        System.out.println("Comments:");
        List<Comment> comments = null;
        try {
            comments = commentService.getComments("nonogram");
        }catch (CommentException e){
            System.out.println("I have problem with print comments");
        }catch (Exception e){
            System.out.println(e.toString());
        }
        int i = 1;
        if (comments!=null) {
            for (Comment comment : comments) {
                System.out.println(i + " " + comment.getPlayer() + ": " + comment.getComment() + ", " + comment.getCommentedOn());
                i++;
            }
            System.out.println("-----------------------------------");
        }else {
            System.out.println("Problem to load comments");
        }
    }
    void printRatings() {
        try {
            System.out.println("Average rating:");
            int rating=ratingService.getAverageRating("nonogram");
            if(rating==-1){
                System.out.println("I didn't found any ratings rating");
            }else {
                System.out.println(rating);
            }
            System.out.println("Your rating:");
            rating=ratingService.getRatingValue("nonogram", this.getPlayersName());
            if(rating==-1){
                System.out.println("I didn't found your rating");
            }else {
                System.out.println(rating);
            }
        }catch (RatingException e){
            System.out.println("I didn't found your rating");
        }catch (Exception e){
            System.out.println(e.toString());
        }
        System.out.println("-----------------------------------");
    }
    void addComment(Scanner scanner) {
        System.out.println("Enter your comment");

        try {
            this.commentService.addComment(new Comment("nonogram", this.getPlayersName(), scanner.nextLine(), new Date()));
        }catch (CommentException e){
            System.out.println("I have problem add your comment:");
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }
    void addRating(Scanner scanner) {
        Integer stars=0;
        Boolean isFirst=true;
        do {
            if(isFirst==false){
                System.out.println("Your input is wrong");
            }
            System.out.println("Enter how stars count <0;5>");
            try {
                stars = Integer.valueOf(scanner.nextLine());
            }
            catch (Exception e)
            {
                stars=-1;
            }
            isFirst=false;
        }while (stars<0||stars>5);

        try {
            this.ratingService.addRating(new Rating("nonogram", this.getPlayersName(), stars, new Date()));
        }catch (RatingException e){
            System.out.println("I have problem add your rate:");
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private void saveGame(int slotNumber){
        try {
            if(dataService.getPlayer("nonogram",slotNumber)==null||dataService.getPlayer("nonogram",slotNumber).equals(this.playersName)) {
                dataService.saveGameData(preparedSavedGame(slotNumber), slotNumber);
            }
        }catch (Exception e){
            throw new RuntimeException("I have problem to uploaded game to database");
            //throw new RuntimeException(e);
        }
    }

    private Data preparedSavedGame(int slotNumber){
        File dataFile=new File("gameToSave.bin");
        try(FileOutputStream fileOutputStream=new FileOutputStream(dataFile);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream)){
            objectOutputStream.writeObject(field);
            objectOutputStream.flush();
            try(FileInputStream fileInputStream=new FileInputStream(dataFile)) {
                return new Data(slotNumber,fileInputStream.readAllBytes(),playersName,field.countingScore(),new Date(),"nonogram");
            }
        } catch (IOException e) {
            throw new RuntimeException("I have problem to prepare board template to be uploaded to database");
            //throw new RuntimeException(e);
        }
    }

    private void getSavedGameData(int slotNumber) throws IOException {
        try {

            Data data=dataService.getSavedGameData("nonogram",slotNumber,playersName);

            byte[] objectData = data.getData();

            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(objectData));
            this.field = (Field) objIn.readObject();
            objIn.close();
        }
        catch (Exception e) {
            field=fieldGenerator.generate();
        }
    }
    private void deleteSave(int slotNumber) throws IOException {
        try {
            dataService.deleteSave("nonogram",slotNumber,playersName);
        }
        catch (Exception e) {
            e.toString();
        }
    }

    private void printSavedGames() throws IOException {
        System.out.println("Saved games:");
        List<Data> resut = null;
        try {
            resut=dataService.getSavedGames("nonogram");
        }
        catch (Exception e) {
            e.toString();
        }
        int i = 1;
        if(resut.size()==0){
            System.out.println("I didn't found savegames");
        }else {
            for (Data data : resut) {
                System.out.println(i + " " + data.getIdent() + ": " + data.getPlayer() + ", " + data.getPlayedOn());
                i++;
            }
        }
        System.out.println("-----------------------------------");
    }
}
