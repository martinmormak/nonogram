package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class HallOfFame {
    @Autowired
    private ScoreService scoreService;

    @RequestMapping("/halloffame")
    public String hallOfFame(Model model) {
        setModel(model);
        return "hallOfFame";
    }

    private void setModel(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("nonogram"));
    }
}
