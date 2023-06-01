package sk.tuke.gamestudio.userInterface;

import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.core.FieldState;
import sk.tuke.gamestudio.core.NumberTile;
import sk.tuke.gamestudio.core.TileState;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JFrameUI extends JPanel implements MouseListener, UserInterface {
    private Field field;
    private Integer cellWidth;
    private Integer cellHeight;
    private ScoreService scoreService;
    private CommentService commentService;
    private RatingService ratingService;
    private String playersName;

    public JFrameUI(Field field,String playersName) {
        this.field = field;
        this.playersName=playersName;
        this.scoreService = new ScoreServiceJDBC();
        this.commentService= new CommentServiceJDBC();
        this.ratingService = new RatingServiceJDBC();
        addMouseListener(this);
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
                throw new Exception("");
        }
        return tileState;
    }

    void endGame() {
        this.getField().setFieldState(FieldState.FAILED);
        JOptionPane.showMessageDialog(null,"Sorry you exit before solve!");
        System.exit(0);
    }

    String handleInput(Scanner scanner){
        if(scanner==null)return null;
        Matcher matcher;
        String inputString;
        do {
            inputString = JOptionPane.showInputDialog("Enter your turn in this format color (w (white),r (red),g (green),b (blue),l (black),y (yellow),c (cyan),p (purple)) xPosition");
            if(inputString==null){
                return null;
            }
            Pattern pattern = Pattern.compile("^(Exit|End|Help|Print Ratings|Print Comments|Add Rating|Add Comment|w|r|g|b|l|y|c|p)$", Pattern.CASE_INSENSITIVE);//"^Exit$|^End$|^Help$|^[w|r|g|b|l|y|c|p]\\s\\d+\\s\\d+$"
            matcher = pattern.matcher(inputString);
        }while (!matcher.find());
        if ("end".equals(inputString.toLowerCase()) || "exit".equals(inputString.toLowerCase())) {
            this.endGame();
        } else if ("help".equals(inputString.toLowerCase())) {
            this.printHelpMenu();
        } else if ("print ratings".equals(inputString.toLowerCase())) {
            this.printRatings();
            return null;
        } else if ("print comments".equals(inputString.toLowerCase())) {
            this.printComments();
            return null;
        } else if ("add rating".equals(inputString.toLowerCase())) {
            this.addRating();
            return null;
        } else if ("add comment".equals(inputString.toLowerCase())) {
            this.addComment();
            return null;
        } else {
            try {
                String input[]=inputString.split(" ");
                TileState tileState = convertCharToTileState(input[0].charAt(0));
                Integer x = Integer.parseInt(input[1]);
                Integer y = Integer.parseInt(input[2]);
                if (x > 0 && x <= this.getField().getColumnCount()) {
                    if (y > 0 && y <= this.getField().getRowCount()) {
                        field.updateActualField(tileState, x, y);
                    } else {
                    }
                }
            } catch (Exception e) {
            }
        }
        return inputString;
    }

    public void play() {
        field.automaticFillActualField();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        this.printTopScores();
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

    void printUpperHints(Integer maxColumnField, Integer maxRowField,Graphics g) {
        if(maxColumnField==null||maxRowField==null){return;}
        for (Integer actualRow = this.getField().getRowCount() - maxColumnField; actualRow < this.getField().getRowCount(); actualRow++) {
            for (Integer actualColumn = this.getField().getColumnCount() - maxRowField; actualColumn < this.getField().getColumnCount() + 1; actualColumn++) {
            }
            for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                int x = actualColumn * cellWidth;
                int y = actualRow * cellHeight;
                if (this.getField().getColumnField()[actualColumn][actualRow] != null && ((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue() != 0) {//if columnField[actualColumn][actualRow]!=null
                    switch (this.getField().getColumnField()[actualColumn][actualRow].getColor()) {
                        case RED:
                            g.setColor(Color.RED);
                            break;
                        case GREEN:
                            g.setColor(Color.GREEN);
                            break;
                        case BLUE:
                            g.setColor(Color.BLUE);
                            break;
                        case YELLOW:
                            g.setColor(Color.YELLOW);
                            break;
                        case PINK:
                            g.setColor(Color.PINK);
                            break;
                        case CYAN:
                            g.setColor(Color.CYAN);
                            break;
                        case BLACK:
                            g.setColor(Color.BLACK);
                            break;
                        case WHITE:
                            g.setColor(Color.WHITE);
                            break;
                        default:
                            break;
                    }
                    g.drawString(((NumberTile) this.getField().getColumnField()[actualColumn][actualRow]).getValue().toString(), field.getColumnCount()*cellWidth+x + cellWidth / 2, y + cellHeight / 2);
                    //g.drawRect(field.getColumnCount()*cellWidth+x, y, cellWidth, cellHeight);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawString("", field.getColumnCount()*cellWidth+x + cellWidth / 2, y + cellHeight / 2);
                    //g.drawRect(field.getColumnCount()*cellWidth+x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    void printFieldWithLeftHints(Integer maxRowField, Graphics g) {
        if(maxRowField==null){return;}
        for (Integer actualRow = 0; actualRow < this.getField().getRowCount(); actualRow++) {
            for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                int x = actualColumn * cellWidth;
                int y = actualRow * cellHeight;
                g.setColor(Color.WHITE);
                g.drawRect(field.getColumnCount() * cellWidth + x, field.getRowCount() * cellHeight + y, cellWidth, cellHeight);
            }
        }
        for (Integer actualRow = 0; actualRow < this.getField().getRowCount(); actualRow++) {
            for (Integer actualColumn = this.getField().getColumnCount() - maxRowField; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                int x = actualColumn * cellWidth;
                int y = actualRow * cellHeight;
                if (this.getField().getRowField()[actualRow][actualColumn] != null && ((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue() != 0) {//if rowField[actualColumn][actualRow]!=null
                    switch (this.getField().getRowField()[actualRow][actualColumn].getColor()) {
                        case RED:
                            g.setColor(Color.RED);
                            break;
                        case GREEN:
                            g.setColor(Color.GREEN);
                            break;
                        case BLUE:
                            g.setColor(Color.BLUE);
                            break;
                        case YELLOW:
                            g.setColor(Color.YELLOW);
                            break;
                        case PINK:
                            g.setColor(Color.PINK);
                            break;
                        case CYAN:
                            g.setColor(Color.CYAN);
                            break;
                        case BLACK:
                            g.setColor(Color.BLACK);
                            break;
                        case WHITE:
                            g.setColor(Color.WHITE);
                            break;
                        default:
                            break;
                    }
                    g.drawString(((NumberTile) this.getField().getRowField()[actualRow][actualColumn]).getValue().toString(), x + cellWidth / 2, field.getRowCount()*cellHeight+y + cellHeight / 2);
                    //g.drawRect(x, field.getRowCount()*cellHeight+y, cellWidth, cellHeight);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawString("", x + cellWidth / 2, field.getRowCount()*cellHeight+y + cellHeight / 2);
                    //g.drawRect(x, field.getRowCount()*cellHeight+y, cellWidth, cellHeight);
                }
            }

            for (Integer actualColumn = 0; actualColumn < this.getField().getColumnCount(); actualColumn++) {
                int x = actualColumn * cellWidth;
                int y = actualRow * cellHeight;
                if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.WHITE) {
                    g.setColor(Color.WHITE);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.RED) {
                    g.setColor(Color.RED);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.GREEN) {
                    g.setColor(Color.GREEN);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.BLUE) {
                    g.setColor(Color.BLUE);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.YELLOW) {
                    g.setColor(Color.YELLOW);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.PINK) {
                    g.setColor(Color.PINK);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.CYAN) {
                    g.setColor(Color.CYAN);
                } else if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() == TileState.BLACK) {
                    g.setColor(Color.BLACK);
                }
                    g.drawString("#", field.getColumnCount() * cellWidth + x + cellWidth / 2, field.getRowCount() * cellHeight + y + cellHeight / 2);
                if (this.getField().getActualFieldTile(actualRow,actualColumn).getColor() != TileState.WHITE) {
                    g.drawRect(field.getColumnCount() * cellWidth + x, field.getRowCount() * cellHeight + y, cellWidth, cellHeight);
                }
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        cellWidth = getWidth() / (this.getField().getColumnCount()*2);
        cellHeight = getHeight() / (this.getField().getRowCount()*2);


        Integer maxColumnField = this.countMaxNotEmptyRowsInColumnField();
        Integer maxRowField = this.countMaxNotEmptyRowsInRowField();

        printUpperHints(maxColumnField, maxRowField,g);
        printFieldWithLeftHints(maxRowField,g);
    }
    void printHelpMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("Help - show help menu\nEnd or Exit - to Exit menu\nAdd comment - to add comment\nPrint comments - to show comments\nAdd rating - to add rating\nPrint ratings - to show ratings\nTo fill tile - Enter your turn in this format color (w (white),r (red),g (green),b (blue),l (black),y (yellow),c (cyan),p (purple)) xPosition\n");

        JOptionPane.showMessageDialog(null, sb.toString(), "Help menu", JOptionPane.INFORMATION_MESSAGE);
    }

    void printTopScores() {
        StringBuilder sb = new StringBuilder();
        List<Score> scores=null;
        try {
            scores = scoreService.getTopScores("nonogram");
        }catch (ScoreException e){
            System.out.println("\nI have problem with print top scores:\n");
            JOptionPane.showMessageDialog(null, sb.toString(), " Top Scores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }catch (Exception e){
            sb.append(e.toString());
            JOptionPane.showMessageDialog(null, sb.toString(), " Top Scores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        sb.append("Top Scores:\n");
        for (Score score : scores) {
            sb.append(score.getPlayer()).append(": ").append(score.getPoints()).append(": ").append(score.getPlayedOn()).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Top Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    void printComments() {
        StringBuilder sb = new StringBuilder();
        List<Comment> comments;
        try {
            comments = commentService.getComments("nonogram");
        }catch (CommentException e){
            sb.append("I have problem with print comments\n");
            JOptionPane.showMessageDialog(null, sb.toString(), "Comments", JOptionPane.INFORMATION_MESSAGE);
            return;
        }catch (Exception e){
            sb.append(e.toString());
            JOptionPane.showMessageDialog(null, sb.toString(), "Comments", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        sb.append("Comments:\n");
        for (Comment comment : comments) {
            sb.append(comment.getPlayer()).append(": ").append(comment.getComment()).append(": ").append(comment.getCommentedOn()).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Comments", JOptionPane.INFORMATION_MESSAGE);
    }
    void printRatings() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("Average rating:\n");
            sb.append(ratingService.getAverageRating("nonogram"));
            sb.append("\nYour rating:\n");
            int rating=ratingService.getRatingValue("nonogram", this.getPlayersName());
            if(rating==-1){
                sb.append("I didn't found your rating\n");
            }else {
                sb.append(rating);
            }
        }catch (RatingException e) {
            sb.append("I didn't found your rating:\n");
            JOptionPane.showMessageDialog(null, sb.toString(), "Ratings", JOptionPane.INFORMATION_MESSAGE);
            return;
        }catch (Exception e){
            sb.append(e.toString());
            JOptionPane.showMessageDialog(null, sb.toString(), "Ratings", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Ratings", JOptionPane.INFORMATION_MESSAGE);
    }
    void addComment() {
        StringBuilder sb = new StringBuilder();
        try {
            this.commentService.addComment(new Comment("nonogram", this.getPlayersName(), JOptionPane.showInputDialog("Enter your comment"), new Date()));
        }catch (CommentException e){
            sb.append("\nI have problem add your comment:\n");
            JOptionPane.showMessageDialog(null, sb.toString(), "Comments", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            sb.append(e.toString());
            JOptionPane.showMessageDialog(null, sb.toString(), "Comments", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    void addRating() {
        Integer stars;
        do {
            stars= Integer.valueOf(JOptionPane.showInputDialog("Enter how stars count <0;5>"));
        }while (stars<0||stars>5);

        StringBuilder sb = new StringBuilder();
        try {
            this.ratingService.addRating(new Rating("nonogram",this.getPlayersName(),  stars, new Date()));
        }catch (RatingException e){
            sb.append("\nI have problem add your rating:\n");
            JOptionPane.showMessageDialog(null, sb.toString(), "Ratings", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            sb.append(e.toString());
            JOptionPane.showMessageDialog(null, sb.toString(), "Ratings", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Integer selectedRow = e.getY() / cellHeight;
        Integer selectedCol = e.getX() / cellWidth;

        String input = this.handleInput(new Scanner(System.in));
        if(input==null){
            return;
        }
        TileState tileState = TileState.WHITE;
        try {
            tileState = convertCharToTileState(input.charAt(0));
        } catch (Exception ex) {
        }
        this.getField().updateActualField(tileState,selectedCol-this.getField().getColumnCount()+1,selectedRow-this.getField().getRowCount()+1);
        repaint();
        if(this.getField().getFieldState() == FieldState.SOLVED){
            StringBuilder sb = new StringBuilder();
            JOptionPane.showMessageDialog(null,"You soved this nonogram");
            try {
                this.scoreService.addScore(new Score("nonogram", this.getPlayersName(), this.getField().countingScore(), new Date()));
            }catch (ScoreException exception){
                sb.append("\nI have problem add your score to database:\n");
                JOptionPane.showMessageDialog(null, sb.toString(), "Score", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                sb.append(exception.toString());
                JOptionPane.showMessageDialog(null, sb.toString(), "Score", JOptionPane.INFORMATION_MESSAGE);
            }
            System.exit(0);
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
