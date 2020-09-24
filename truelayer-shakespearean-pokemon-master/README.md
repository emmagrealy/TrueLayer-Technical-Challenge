# TrueLayer Shakespearean Pokemon API: Backend Engineer - Credit Challenge

## Assignment Details

This assignment revolves around developing a search engine that when given a pokemon name will return it's description in Shakespearean style

To help in the creating of my application I used two APIs:
*[PokeAPI](https://pokeapi.co/) - An API that is connected to a thoroughly documented database all about pokemon.

*[Shakespearean Translator  ](https://funtranslations.com/api/shakespeare) - An API converts modern text to Shakespearean style
... Note : The Shakespearean API limits the number of API calls to 60 a day with a distribution of 5 per hour.

## Implementation
As I do not need a database for this project the language I used for this project was Java and I used Springboot.

I also used a third party technology,JUnit, to help me with any tests I ran.

## Build and Run 
1. Clone my project into eclipse
2. Go to the class named **TrueLayerShakespearePokemonApplication**
... this will be inside src/main/java, inside com.truelayerShakespearPokemonApp
3. Run the project from here 
... you will see the spring framework has successfully compiled
4. Go to your search engine and enter the following 
... http://localhost:8080/pokemon/<pokemon-name>
... Enter any pokemon name you like and you will recieve the description.


## NOTE
I have included comments through out my code to help explain it clearly.