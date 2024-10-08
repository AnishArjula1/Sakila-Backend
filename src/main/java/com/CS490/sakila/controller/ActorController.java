package com.CS490.sakila.controller;

import com.CS490.sakila.dto.ActorFilmDTO;
import com.CS490.sakila.model.Actor;
import com.CS490.sakila.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class ActorController {

    private static final Logger logger = Logger.getLogger(ActorController.class.getName());

    @Autowired
    private ActorRepository actorRepository;

    // Endpoint to get top 5 actors by order of rentals or any other criteria
    @GetMapping("/actors/top5")
    public List<Actor> getTop5Actors() {
        logger.info("Fetching top 5 actors");
        return actorRepository.findTop5ByOrderByActorIdDesc();  
    }

    // Endpoint to fetch actor details and their top 5 rented films
    @GetMapping("/actors/{actor_id}/top5films")
    public ActorFilmDTO getActorDetailsWithTop5Films(@PathVariable("actor_id") int actorId) {
        logger.info("Fetching details and top 5 films for actor with ID: " + actorId);

        Optional<Actor> actor = actorRepository.findById(actorId);
        if (!actor.isPresent()) {
            logger.warning("Actor with ID " + actorId + " not found.");
            return null;
        }

        // Fetch top 5 rented films by actor
        List<Object[]> films = actorRepository.findTop5RentedFilmsByActor(actorId);
        List<String> filmTitles = new ArrayList<>();

        for (Object[] film : films) {
            String filmTitle = (String) film[1];
            filmTitles.add(filmTitle);
            logger.info("Found film: " + filmTitle);
        }

        return new ActorFilmDTO(actor.get().getFirstName(), actor.get().getLastName(), filmTitles);
    }
}





