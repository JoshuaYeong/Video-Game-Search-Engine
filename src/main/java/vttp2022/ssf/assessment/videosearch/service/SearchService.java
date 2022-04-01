package vttp2022.ssf.assessment.videosearch.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.assessment.videosearch.models.Game;

@Service
public class SearchService {

    private static final String URL = "https://api.rawg.io/api/games";
    // private static final String apiKey = "29778eb076aa468cbb188f5807e00498";

    @Value("${rawg.api.key}")
    private String apiKey;

    public List<Game> search(String searchString, Integer count)
    {
        String gameUrl = UriComponentsBuilder
            .fromUriString(URL)
            .queryParam("search", searchString)
            .queryParam("page_size", count)
            .queryParam("key", apiKey)
            .toUriString();

        RequestEntity<Void> req = RequestEntity
        .get(gameUrl)
        .accept(MediaType.APPLICATION_JSON)
        .build();

        // System.out.println("This is the req: " + req);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        // System.out.printf(">>>>> This is the resp: " + resp.getBody());

        List<Game> listOfGames = new ArrayList<>();

        try(InputStream is = new ByteArrayInputStream(resp.getBody().getBytes()))
        {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            JsonArray jArray = data.getJsonArray("results");

            for (int i=0; i<jArray.size(); i++)
            {
                Game game = new Game();
                JsonObject arrObject = jArray.getJsonObject(i);
                String name = arrObject.getString("name");
                Float rating = Float.parseFloat(arrObject.getJsonNumber("rating").toString());
                String backgroundImage = arrObject.getString("background_image");
                // Integer released = arrObject.getInt("released");

                game.setName(name);
                game.setRating(rating);
                game.setBackgroundImage(backgroundImage);
                game.toString();
                listOfGames.add(game);
            }

            System.out.println("This is the list of games: " + listOfGames);

        } catch (IOException ex) 
        {
            System.err.printf("+++ error: %s\n", ex.getMessage());
        } 
        if (!listOfGames.isEmpty()) 
        {
            return listOfGames; 
        }
        return Collections.emptyList();
    }

}
