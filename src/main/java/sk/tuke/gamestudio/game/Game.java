package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.service.FieldGeneratorJDBC;
import sk.tuke.gamestudio.userInterface.ConsoleUI;
import sk.tuke.gamestudio.userInterface.JFrameUI;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    String handleInput(Scanner scanner){
    if(scanner==null)return null;
    Matcher matcher;
    String inputString;
    do {
        System.out.println("Enter if you want play game in console or frame");
        inputString = scanner.nextLine();
        Pattern pattern = Pattern.compile("^(Console|Frame)$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(inputString);
    }while (!matcher.find());
    return inputString;
}
    void printWelcomeText(){
        System.out.print("\n" +
                "\u001B[30m"+"███╗   ██╗"+"\u001B[31m"+" ██████╗ "+"\u001B[32m"+"███╗   ██╗"+"\u001B[33m"+" ██████╗ "+"\u001B[34m"+" ██████╗ "+"\u001B[35m"+"██████╗ "+"\u001B[36m"+" █████╗ "+"\u001B[37m"+"███╗   ███╗\n" +
                "\u001B[30m"+"████╗  ██║"+"\u001B[31m"+"██╔═══██╗"+"\u001B[32m"+"████╗  ██║"+"\u001B[33m"+"██╔═══██╗"+"\u001B[34m"+"██╔════╝ "+"\u001B[35m"+"██╔══██╗"+"\u001B[36m"+"██╔══██╗"+"\u001B[37m"+"████╗ ████║\n" +
                "\u001B[30m"+"██╔██╗ ██║"+"\u001B[31m"+"██║   ██║"+"\u001B[32m"+"██╔██╗ ██║"+"\u001B[33m"+"██║   ██║"+"\u001B[34m"+"██║  ███╗"+"\u001B[35m"+"██████╔╝"+"\u001B[36m"+"███████║"+"\u001B[37m"+"██╔████╔██║\n" +
                "\u001B[30m"+"██║╚██╗██║"+"\u001B[31m"+"██║   ██║"+"\u001B[32m"+"██║╚██╗██║"+"\u001B[33m"+"██║   ██║"+"\u001B[34m"+"██║   ██║"+"\u001B[35m"+"██╔══██╗"+"\u001B[36m"+"██╔══██║"+"\u001B[37m"+"██║╚██╔╝██║\n" +
                "\u001B[30m"+"██║ ╚████║"+"\u001B[31m"+"╚██████╔╝"+"\u001B[32m"+"██║ ╚████║"+"\u001B[33m"+"╚██████╔╝"+"\u001B[34m"+"╚██████╔╝"+"\u001B[35m"+"██║  ██║"+"\u001B[36m"+"██║  ██║"+"\u001B[37m"+"██║ ╚═╝ ██║\n" +
                "\u001B[30m"+"╚═╝  ╚═══╝"+"\u001B[31m"+" ╚═════╝ "+"\u001B[32m"+"╚═╝  ╚═══╝"+"\u001B[33m"+" ╚═════╝ "+"\u001B[34m"+" ╚═════╝ "+"\u001B[35m"+"╚═╝  ╚═╝"+"\u001B[36m"+"╚═╝  ╚═╝"+"\u001B[37m"+"╚═╝     ╚═╝\n" +
                "                                                                          \n");
        /*System.out.print("\n" +
                "\u001B[31m"+"███╗   ██╗"+"\u001B[32m"+" ██████╗ "+"\u001B[31m"+"███╗   ██╗"+"\u001B[32m"+" ██████╗ "+"\u001B[31m"+" ██████╗ "+"\u001B[32m"+"██████╗ "+"\u001B[31m"+" █████╗ "+"\u001B[32m"+"███╗   ███╗\n" +
                "\u001B[31m"+"████╗  ██║"+"\u001B[32m"+"██╔═══██╗"+"\u001B[31m"+"████╗  ██║"+"\u001B[32m"+"██╔═══██╗"+"\u001B[31m"+"██╔════╝ "+"\u001B[32m"+"██╔══██╗"+"\u001B[31m"+"██╔══██╗"+"\u001B[32m"+"████╗ ████║\n" +
                "\u001B[31m"+"██╔██╗ ██║"+"\u001B[32m"+"██║   ██║"+"\u001B[31m"+"██╔██╗ ██║"+"\u001B[32m"+"██║   ██║"+"\u001B[31m"+"██║  ███╗"+"\u001B[32m"+"██████╔╝"+"\u001B[31m"+"███████║"+"\u001B[32m"+"██╔████╔██║\n" +
                "\u001B[31m"+"██║╚██╗██║"+"\u001B[32m"+"██║   ██║"+"\u001B[31m"+"██║╚██╗██║"+"\u001B[32m"+"██║   ██║"+"\u001B[31m"+"██║   ██║"+"\u001B[32m"+"██╔══██╗"+"\u001B[31m"+"██╔══██║"+"\u001B[32m"+"██║╚██╔╝██║\n" +
                "\u001B[31m"+"██║ ╚████║"+"\u001B[32m"+"╚██████╔╝"+"\u001B[31m"+"██║ ╚████║"+"\u001B[32m"+"╚██████╔╝"+"\u001B[31m"+"╚██████╔╝"+"\u001B[32m"+"██║  ██║"+"\u001B[31m"+"██║  ██║"+"\u001B[32m"+"██║ ╚═╝ ██║\n" +
                "\u001B[31m"+"╚═╝  ╚═══╝"+"\u001B[32m"+" ╚═════╝ "+"\u001B[31m"+"╚═╝  ╚═══╝"+"\u001B[32m"+" ╚═════╝ "+"\u001B[31m"+" ╚═════╝ "+"\u001B[32m"+"╚═╝  ╚═╝"+"\u001B[31m"+"╚═╝  ╚═╝"+"\u001B[32m"+"╚═╝     ╚═╝\n" +
                "                                                                          \n");*/
    }
    public void play() throws IOException, ClassNotFoundException {
        this.printWelcomeText();
        System.out.println("Enter your name");
        Scanner scanner=new Scanner(System.in);
        String playersName=scanner.nextLine();
        String input=this.handleInput(scanner);
        //Field field=new FieldGenerator().generate(6);
        Field field=new FieldGeneratorJDBC().generate();
        if("console".equals(input.toLowerCase())) {
            ConsoleUI gameControler = new ConsoleUI(field,playersName);
            gameControler.play();
        }else {

            JFrameUI jFrameUI = new JFrameUI(field,playersName);
            jFrameUI.play();
        }
    }
}
