package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Data;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.DataService;
import sk.tuke.gamestudio.service.LoginService;

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private String playerName=null;
    private boolean logged=false;
    @Autowired
    private DataService dataService;
    @Autowired
    private LoginService loginService;

    @RequestMapping("/")
    public String home(Model model) {
        setModel(model);
        return "home";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(required = false) String playerName, @RequestParam(required = false) String password, Model model) {
        if(playerName!=null&&password!=null) {
            if (loginService.comparePasswords(playerName, password)) {
                this.playerName = playerName;
                logged = true;
            } else {
                this.playerName = null;
                logged = false;
            }
        } else {
            this.playerName = null;
            logged = false;
        }
        setModel(model);
        return "home";
    }

    @RequestMapping("/register")
    public String register(@RequestParam(required = false) String playerName, @RequestParam(required = false) String password, Model model) {
        if(playerName!=null&&password!=null) {
            loginService.addUser(new Player(playerName, password));
            this.playerName = playerName;
            logged = true;
        }else {
            this.playerName = null;
            logged = false;
        }
        setModel(model);
        return "home";
    }

    @RequestMapping("/logout")
    public String logout(Model model) {
        this.playerName=null;
        logged=false;
        setModel(model);
        return "home";
    }

    @RequestMapping("/getPlayerName")
    public String getPlayerName(){
        return playerName;
    }
    public boolean isLogged(){
        return logged;
    }

    public String getSaves(){
        List<Data> resut = null;
        try {
            resut=dataService.getSavedGames("nonogram");
        }
        catch (Exception e) {
            e.toString();
        }
        StringBuilder sb = new StringBuilder();if(resut.size()==0){
            sb.append("<h4>I didn't found savegames</h4>\n");
        }else {
            sb.append("<table class='saves'>\n");
            for (Data data : resut) {
                sb.append("<tr>\n");
                sb.append("<td>\n");
                sb.append("<a href='/nonogram/loadGame?slotNumber=" + data.getIdent() +"'>\n");
                sb.append(data.getIdent() + "\n");
                sb.append("</td>\n");
                sb.append("<td>\n");
                sb.append(data.getPlayer() + "\n");
                sb.append("</td>\n");
                sb.append("<td>\n");
                sb.append(data.getPlayedOn()+"\n");
                sb.append("</td>\n");
                sb.append("</tr>\n");
            }
            sb.append("</table>\n");
        }
        return sb.toString();
    }


    private void setModel(Model model) {
        model.addAttribute("saves", dataService.getSavedGames("nonogram"));
    }
}
