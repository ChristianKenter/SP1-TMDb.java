US:2 (Setup TMDb API key):
* As a developer
* I want to setup the TMDb API key
* So the app can connect to TMDb.
Acceptance criteria 1:
* Given that I have a TMDb API key
* When I run the app
* Then the app should be able to use the API key without having it in the code.

US:3 (Connect to TMDb API):
* As a developer
* I want to connect the program to the TMDb API
* So we can get movie data from it.
Acceptance criteria 1:
* Given that the API key is setup
* When I send a request to TMDb
* Then the program should get movie data back.

US:4 (Create movie DTOs):
* As a developer
* I want to make DTOs for the TMDb data
* So we can use the JSON data in Java.
Acceptance criteria 1:
* Given that TMDb sends JSON
* When the program gets the response
* Then Jackson should turn the JSON into the right DTOs.

US:5 (Fetch movies):
* As a developer
* I want to get movies from the last 5 years
* So we only save the movies we need.
Acceptance criteria 1:
* Given that the TMDb API is working
* When I run the fetch method
* Then the program should get movies from the last 5 years.

US:6 (Create database entities):
* As a developer
* I want to make JPA entities for movies, actors, directors and genres
* So we can save them in the database.
Acceptance criteria 1:
* Given that the entities are made
* When the program starts
* Then JPA should make the tables from the entities.

US:7 (Connect movie relationships):
* As a developer
* I want to connect movies with actors, directors and genres
* So we know who and what belongs to each movie.
Acceptance criteria 1:
* Given that a movie has actors, a director and genres
* When the movie is saved
* Then the connections should also be saved.

US:8 (Convert DTOs to entities):
* As a developer
* I want to change the DTOs into entities
* So we can save the movie data in the database.
Acceptance criteria 1:
* Given that the movie DTOs have been fetched
* When the program converts them
* Then the DTOs should become JPA entities.

US:9 (Save movies):
* As a developer
* I want to save the movies in the database
* So we don't have to call them from TMDb every time.
Acceptance criteria 1:
* Given that the movie data has been fetched
* When we save the data
* Then the movies should be in the database.

US:10 (Create movie DAO):
* As a developer
* I want to make DAO methods for movies
* So we can work with the movies in the database.
Acceptance criteria 1:
* Given that movies are in the database
* When a DAO method is used
* Then it should be able to add, get, update and delete movies.

US:11 (Create service layer):
* As a developer
* I want to make a service layer
* So it can handle the logic between the app and DAO.
Acceptance criteria 1:
* Given that the app needs movie data
* When the service is called
* Then it should use the DAO and handle the DTO and entity stuff.

US:12 (Get all movies):
* As a user
* I want to see all the movies
* So I can see what movies are in the database.
Acceptance criteria 1:
* Given that there are movies in the database
* When I ask for all movies
* Then the program should show all the movies.

US:13 (Get actors and directors):
* As a user
* I want to see all the actors and directors
* So I can see who is connected to the movies.
Acceptance criteria 1:
* Given that actors and directors are saved
* When I ask for them
* Then the program should show the actors and directors.

US:14 (Get all genres):
* As a user
* I want to see all the genres
* So I can see what genres there are.
Acceptance criteria 1:
* Given that genres are in the database
* When I ask for all genres
* Then the program should show a list of genres.

US:15 (Find movies by genre):
* As a user
* I want to find movies by a genre
* So I can see what movies are in that genre.
Acceptance criteria 1:
* Given that a genre exists
* When I search for a genre
* Then the program should show all movies with that genre.

US:16 (Update movies):
* As a user
* I want to update movies
* So I can …
