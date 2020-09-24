package com.truelayer.shakespearepokemon.restfulservice;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesFlavorText;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.truelayer.shakespearepokemon.utilfiles.Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpService {

	//Logger used to log the information about each action available in the application
    private final Logger log = LoggerFactory.getLogger(HttpService.class);

    
    //abstract class - base implementation of httpClient
    private final CloseableHttpClient httpClient;

    //enables http requests
    public HttpService() {
        httpClient = HttpClients.createDefault();
    }

    public void close() throws IOException {
        httpClient.close();
    }

    //get the pokemons information from its name and display the results
    public int getPokemonInfo(String name) throws IOException {

    	//logging information
    	log.info("Getting the pokemons infornation from its name: " + name.toLowerCase());

    	//create a request
        HttpGet request = new HttpGet(POKE_API_URL + name.toLowerCase());

        //try catch to ensure the request works
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String result = EntityUtils.toString(entity);

                try {
                    JSONObject jsonResult = new JSONObject(result);
                    return jsonResult.getInt("id");
                } catch (JSONException e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
            }

        }

    }

    //get the pokemons description from its ID and display the results
    public String getPokemonDescription(int id) {

    	//logging information
    	log.info("Getting the pokemons description in English from its ID: " + id);

    	//Poke API usage
        PokeApi pokeApi = new PokeApiClient();
        PokemonSpecies species = pokeApi.getPokemonSpecies(id);

        List<PokemonSpeciesFlavorText> speciesFlavorTexts = species.getFlavorTextEntries();

        // certain letters and characters are not accepted by json so they must be changes
        for (PokemonSpeciesFlavorText speciesFlavorText : speciesFlavorTexts) {
            if (speciesFlavorText.getLanguage().getName().equals("en")) {
                return speciesFlavorText.getFlavorText()
                        .replace("\r\n", " ")
                        .replace("\n", " ")
                        .replace("é", "e");
            }
        }

        return NO_DESCRIPTION_FOUND;

    }

    //translate the description of the chosen pokemon into shakespearean text
    public String translateToShakespearean(String text) throws IOException {

    	//logging information
    	log.info("Translating the pokemons description into Shakespearean text: " + text);

    	//post request
        HttpPost post = new HttpPost(SHAKESPEARE_API_URL);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("text", text));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        //try catch to ensure the request works
        try (CloseableHttpResponse response = httpClient.execute(post)) {

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String result = EntityUtils.toString(entity);

                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if (!jsonResult.has("error")) {
                        return ((JSONObject)jsonResult.get("contents")).getString("translated");
                    } else {
                        JSONObject error = (JSONObject) jsonResult.get("error");
                        throw new ResponseStatusException(
                                Objects.requireNonNull(HttpStatus.resolve(error.getInt("code"))),
                                error.getString("message")
                        );
                    }
                } catch (JSONException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
            }

        }

    }

}
