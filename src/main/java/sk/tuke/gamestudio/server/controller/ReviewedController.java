package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class ReviewedController {
    @Autowired
    private UserController userController;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/reviewed")
    public String reviewed(Model model) {
        setModel(model);
        return "reviewed";
    }

    private String getPlayerName(){
        if(userController.getPlayerName()==null){
            return null;
        }
        else {
            return userController.getPlayerName();
        }
    }

    @RequestMapping("/reviewed/addreviewed")
    public String addReviewed(@RequestParam(required = false)String comment, @RequestParam(required = false)Integer rating,Model model){
        if(getPlayerName()==null){
            setModel(model);
            return "reviewed";
        }
        if(comment.length()!=0){
            commentService.addComment(new Comment("nonogram",getPlayerName(),comment, new Date()));
        }
        if(rating!=null&&rating>=0&&rating<=5){
            ratingService.addRating(new Rating("nonogram",getPlayerName(),rating,new Date()));
        }
        setModel(model);
        return "reviewed";
    }

    public Integer getAverageRatingValue(){
        Integer rate = ratingService.getAverageRating("nonogram");
        return rate;
    }

    public Integer getRatingValue(String playerName){
        return ratingService.getRatingValue("nonogram",playerName);
    }

    private void setModel(Model model) {
        model.addAttribute("comments", commentService.getComments("nonogram"));
    }
}
