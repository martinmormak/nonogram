package sk.tuke.gamestudio.server.controller;

import org.hibernate.hql.internal.ast.tree.IdentNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.core.*;
import sk.tuke.gamestudio.entity.Data;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.util.Date;

@Controller
@RequestMapping("/nonogram")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class NonogramController {
    @Autowired
    private UserController userController;

    private Field field;
    private TileState actualColor =TileState.WHITE;
    private FieldGenerator fieldGenerator;
    private Integer indexBufer=-1;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private DataService dataService;
    public NonogramController(FieldGenerator fieldGenerator) throws FileNotFoundException {
        this.fieldGenerator=fieldGenerator;
        this.field=fieldGenerator.generate();
        field.setStartTime();
    }

    @RequestMapping
    public String nonogram(@RequestParam(required = false)Integer row, @RequestParam(required = false)Integer column, Model model) throws FileNotFoundException {
        if(row!=null&&column!=null){
            field.updateActualField(actualColor,column+1,row+1);
            if(field.getFieldState()== FieldState.SOLVED){
                try {
                    scoreService.addScore(new Score("nonogram", getPlayerName(), field.countingScore(), new Date()));
                    if(indexBufer!=-1) {
                        this.deleteSave(indexBufer);
                    }
                    setModel(model);
                    return "home";
                }catch (ScoreException e){
                    System.out.println("I have problem with add your score to database");
                }catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }
        setModel(model);
        return "nonogram";
    }
    private void setModel(Model model){
        model.addAttribute("field", field);
        model.addAttribute("saves", dataService.getSavedGames("nonogram"));
    }

    @RequestMapping("/new")
    public String newGame(Model model) throws FileNotFoundException {
        field=fieldGenerator.generate();
        field.setStartTime();
        setModel(model);
        indexBufer=-1;
        return "nonogram";
    }

    private String getPlayerName(){
        if(userController.getPlayerName()==null){
            return System.getProperty("user.name");
        }
        else {
            return userController.getPlayerName();
        }
    }


    @RequestMapping("/change")
    public String changeColor(@RequestParam(required = false)String color, Model model) {
        switch (color){
            case "white":
                actualColor =TileState.WHITE;
                break;
            case "red":
                actualColor =TileState.RED;
                break;
            case "green":
                actualColor =TileState.GREEN;
                break;
            case "blue":
                actualColor =TileState.BLUE;
                break;
            case "yellow":
                actualColor =TileState.YELLOW;
                break;
            case "pink":
                actualColor =TileState.PINK;
                break;
            case "cyan":
                actualColor =TileState.CYAN;
                break;
            case "black":
                actualColor =TileState.BLACK;
                break;
        }
        setModel(model);
        return "nonogram";
    }

    @RequestMapping("/loadGame")
    private String getSavedGameData(@RequestParam(required = false) Integer slotNumber, Model model) throws IOException {
        if (slotNumber != null) {
            try {

                Data data = dataService.getSavedGameData("nonogram", slotNumber, getPlayerName());

                byte[] objectData = data.getData();

                ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(objectData));
                this.field = (Field) objIn.readObject();
                objIn.close();
            } catch (Exception e) {
                field = fieldGenerator.generate();
            }
            field.setStartTime();
            indexBufer = slotNumber;
        }else {
            indexBufer = null;
            setModel(model);
            return "home";
        }
        setModel(model);
        return "nonogram";
    }

    @RequestMapping("/saveGame")
    private String saveGame(@RequestParam(required = false) Integer slotNumber, Model model){
        if (slotNumber != null) {
            try {
                if (dataService.getPlayer("nonogram", slotNumber) == null || dataService.getPlayer("nonogram", slotNumber).equals(getPlayerName())) {
                    dataService.saveGameData(preparedSavedGame(slotNumber), slotNumber);
                }
            } catch (Exception e) {
                System.out.println("I have problem to uploaded game to database");
                //throw new RuntimeException("I have problem to uploaded game to database");
                //throw new RuntimeException(e);
                setModel(model);
                return "nonogram";
            }
            setModel(model);
            return "home";
        } else {
            setModel(model);
            return "nonogram";
        }
    }

    private Data preparedSavedGame(int slotNumber){
        File dataFile=new File("gameToSave.bin");
        try(FileOutputStream fileOutputStream=new FileOutputStream(dataFile);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream)){
            objectOutputStream.writeObject(field);
            objectOutputStream.flush();
            try(FileInputStream fileInputStream=new FileInputStream(dataFile)) {
                return new Data(slotNumber,fileInputStream.readAllBytes(),getPlayerName(),field.countingScore(),new Date(),"nonogram");
            }
        } catch (IOException e) {
            System.out.println("I have problem to prepare board template to be uploaded to database");
            //throw new RuntimeException("I have problem to prepare board template to be uploaded to database");
            //throw new RuntimeException(e);
        }
        return null;
    }

    public String newGame(FieldGenerator fieldGenerator) throws FileNotFoundException {
        field = fieldGenerator.generate();
        return "nonogram";
    }

    public String getHtmlField(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='nonogramfield'>\n");
        for (Integer actualRow = this.field.getRowCount() - this.countMaxNotEmptyRowsInColumnField(); actualRow < this.field.getRowCount(); actualRow++) {
            sb.append("<tr>\n");
            for (Integer actualColumn = this.field.getColumnCount() - this.countMaxNotEmptyRowsInRowField()+1; actualColumn < this.field.getColumnCount() + 1; actualColumn++) {
                sb.append("<td class='white'>\n");
                sb.append("</td>\n");
            }
            for (Integer actualColumn = 0; actualColumn < this.field.getColumnCount(); actualColumn++) {
                if (this.field.getColumnField()[actualColumn][actualRow] != null && ((NumberTile) this.field.getColumnField()[actualColumn][actualRow]).getValue() != 0) {//if columnField[actualColumn][actualRow]!=null
                    var tile = field.getColumnField()[actualColumn][actualRow];
                    sb.append("<td class='" + getTileColor(tile) + "'>\n");
                    sb.append("<span>"+((NumberTile) tile).getValue().toString()+"</span>\n");
                    sb.append("</td>\n");
                } else {
                    sb.append("<td class='white'>\n");
                    sb.append("</td>\n");
                }
            }
            sb.append("</tr>\n");
        }
        sb.append("<br>\n");




        for (var actualRow = 0; actualRow < field.getRowCount(); actualRow++) {
            sb.append("<tr>\n");
            for (Integer actualColumn = this.field.getColumnCount() - countMaxNotEmptyRowsInRowField(); actualColumn < this.field.getColumnCount(); actualColumn++) {
                if (this.field.getRowField()[actualRow][actualColumn] != null && ((NumberTile) this.field.getRowField()[actualRow][actualColumn]).getValue() != 0) {//if rowField[actualColumn][actualRow]!=null
                    var tile = field.getRowField()[actualRow][actualColumn];
                    sb.append("<td class='" + getTileColor(tile) + "'>\n");
                    sb.append("<span>"+((NumberTile) tile).getValue().toString()+"</span>\n");
                    sb.append("</td>\n");
                } else {
                    sb.append("<td class='white'>\n");
                    sb.append("</td>\n");
                }
            }
            for (var actualColumn = 0; actualColumn < field.getColumnCount(); actualColumn++) {
                var tile = field.getActualFieldTile(actualRow, actualColumn);
                sb.append("<td class='" + getTileColor(tile) + "'>\n");
                sb.append("<a href='/nonogram?row=" + actualRow + "&column=" + actualColumn + "'>\n");
                //sb.append("<span>"+getTileColor(tile)+"</span>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    public Integer getColumnValue(Integer actualColumn, Integer actualRow){
        return ((NumberTile) field.getColumnField()[actualColumn][actualRow]).getValue();
    }

    public Integer getRowValue(Integer actualColumn, Integer actualRow){
        return ((NumberTile) field.getRowField()[actualRow][actualColumn]).getValue();
    }

    public String getTileColor(Tile tile){
        switch (tile.getColor()){
            case WHITE:
                return "white";
            case RED:
                return "red";
            case GREEN:
                return "green";
            case BLUE:
                return "blue";
            case YELLOW:
                return "yellow";
            case PINK:
                return "pink";
            case CYAN:
                return "cyan";
            case BLACK:
                return "black";
            default :
                throw new RuntimeException("Unexpected tile state");
        }
    }

    public Integer countMaxNotEmptyRowsInColumnField() {
        Integer maxColumnField = 0;
        for (Integer actualColumn = 0; actualColumn < this.field.getColumnCount(); actualColumn++) {
            Integer maxActualNotEmptyRows = 0;
            for (Integer actualRow = 0; actualRow < this.field.getRowCount(); actualRow++) {
                if (this.field.getColumnField()[actualColumn][actualRow] != null && ((NumberTile) this.field.getColumnField()[actualColumn][actualRow]).getValue() != 0) {//if columnField[actualColumn][actualRow]!=null
                    maxActualNotEmptyRows++;
                }
            }
            if (maxActualNotEmptyRows >= maxColumnField) {
                maxColumnField = maxActualNotEmptyRows;
            }
        }
        return maxColumnField;
    }

    public Integer countMaxNotEmptyRowsInRowField() {
        Integer maxRowField = 0;
        for (Integer actualRow = 0; actualRow < this.field.getRowCount(); actualRow++) {
            Integer maxActualNotEmptyRows = 0;
            for (Integer actualColumn = 0; actualColumn < this.field.getColumnCount(); actualColumn++) {
                if (this.field.getRowField()[actualRow][actualColumn] != null && ((NumberTile) this.field.getRowField()[actualRow][actualColumn]).getValue() != 0) {//if rowField[actualColumn][actualRow]!=null
                    maxActualNotEmptyRows++;
                }
            }
            if (maxActualNotEmptyRows >= maxRowField) {
                maxRowField = maxActualNotEmptyRows;
            }
        }
        return maxRowField;
    }

    private void deleteSave(int slotNumber) throws IOException {
        try {
            dataService.deleteSave("nonogram",slotNumber,getPlayerName());
        }
        catch (Exception e) {
            e.toString();
        }
    }
}
