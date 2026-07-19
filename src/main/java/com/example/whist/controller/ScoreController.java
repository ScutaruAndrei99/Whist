package com.example.whist.controller;

import com.example.whist.game.RoundSchedule;
import com.example.whist.game.WhistGame;
import com.example.whist.service.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ScoreController {

    private final ScoreService service;

    public ScoreController(ScoreService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        WhistGame game = service.getCurrentGame();
        model.addAttribute("game", game);
        model.addAttribute("matches", service.getMatchHistory());
        model.addAttribute("minPlayers", RoundSchedule.MIN_PLAYERS);
        model.addAttribute("maxPlayers", RoundSchedule.MAX_PLAYERS);
        model.addAttribute("nameSlots", RoundSchedule.MAX_PLAYERS);
        return "score";
    }

    @PostMapping("/start-game")
    public String startGame(@RequestParam List<String> names, RedirectAttributes redirectAttributes) {
        try {
            service.startGame(names);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/submit-round")
    public String submitRound(@RequestParam List<Integer> bet,
                              @RequestParam List<Integer> hands,
                              RedirectAttributes redirectAttributes) {
        try {
            service.submitRound(bet, hands);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/new-game")
    public String newGame() {
        service.abandonGame();
        return "redirect:/";
    }
}
