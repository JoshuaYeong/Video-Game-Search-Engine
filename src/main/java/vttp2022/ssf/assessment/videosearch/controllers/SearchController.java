package vttp2022.ssf.assessment.videosearch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.assessment.videosearch.models.Game;
import vttp2022.ssf.assessment.videosearch.service.SearchService;

@Controller
@RequestMapping(path="/search")
public class SearchController {

    @Autowired SearchService searchSvc;

    // http://localhost:8080/search?search_name=abc&number_of_results=5

    @GetMapping
    public String gameSearch(@RequestParam(name="search_name") String searchName, 
            @RequestParam(name="number_of_results", defaultValue="10") Integer numOfResults, Model model)
        {

            List<Game> listOfGames = searchSvc.search(searchName, numOfResults);
            if (listOfGames.isEmpty())
                return "empty";

            model.addAttribute("search_name", searchName);
            model.addAttribute("number_of_results", numOfResults);
            model.addAttribute("game", List.of());

            return "game";
        }

}
