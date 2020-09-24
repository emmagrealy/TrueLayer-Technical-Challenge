package com.truelayer.shakespearepokemon.restfulservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.truelayer.shakespearepokemon.utilfiles.Constants.NO_DESCRIPTION_FOUND;

import java.io.IOException;

@RestController
public class PokemonController {

	//how to search for a pokemon - put pokemon name inside {here}
    @GetMapping("/pokemon/{name}")
    public Pokemon pokemon(@PathVariable String name) throws IOException {

        HttpService httpService = new HttpService();
        String translation;

        //try catch to ensure the request works
        try {

            int id = httpService.getPokemonInfo(name);
            String description = httpService.getPokemonDescription(id);
            translation = description.equals(NO_DESCRIPTION_FOUND) || description.isEmpty()
                    ? NO_DESCRIPTION_FOUND
                    : httpService.translateToShakespearean(description);

        } finally {
            httpService.close();
        }

        return new Pokemon(name, translation);
    }

}
