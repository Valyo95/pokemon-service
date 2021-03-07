Pokemon Service
==========

Pokemon Service Implements the given requrement.
First it uses the PokéAPI: https://pokeapi.co/api/v2/ 
  and provides with a REST API for the following scenarios
* Getting the 5 heaviest Pokémons.
* Getting the 5 highest Pokémons.
* Getting the 5 Pokémons with more base experience.


Implementation
----------------------
The service uses two different implementations to gather and query the data:
1. An in-memory processing - that does not use any database to store the Pokemon's info (e.g) `PokemonController`
2. Database implementation - where it is getting all the data, extract, transform it and store
it in an in-memory H2 database, with a specific database model that 
I've designed for the for purposes of the application (instead of mapping all the PokeApi schema into an ER model)

The application run on the localhost on 8080 port.

Both of the implementation are available in the application and each of them provides its functionality
in a RestController:
* `http://localhost:8080/pokemon/` - for the in-memory implementation that uses the `PokemonController`
* `http://localhost:8080/pokemon_db` - for the database implemenation that uses the `PokemonDBController`


Test Locally
------------
Build the application

    mvn clean install
Start the application with maven

    mvn spring-boot:run
Start the application using the docker-compose environemnt

    docker-compose up -d


More information about the application
----------------------------------
First, I got familiar with the PokeApi and its scheme.

Then I used the http://www.jsonschema2pojo.org/ in order to create the Java Pojo classes that
will be used in order to consume the PokeApi and gather each of the pokemons as java objects.

The way that I gather all of the Pokemons is the following:
1. I get the number of pokemons by getting the `count` field of the https://pokeapi.co/api/v2/pokemon/ response.
2. After I have the number of pokemons I fetch all of their ids & corresponding url by getting all of the list
of pokemons with one HTTP request on https://pokeapi.co/api/v2/pokemon/?offset=0&limit=numberOfPokemons
3. Now I have a list of `PokemonEntry` that has the name and the url of each Pokemon.

Since the PokeApi does not provide with an endpoint to get all of the pokemons at once (as the data is pretty large)
one should make an HTTP request for each of the Pokemon in order to gather them all.

### Synchronous Solution
At first, I thought about using the simple `RestTemplate` in order to get all of the pokemons one by one.
This didn't work as I expected since the RestTemplate uses synchronous/blocking mechanism
that will block until the web client receives the response. 

This made that solution super slow and I started to look for other ways to achieve the requirements.

### Asynchronous/Reactive Solution
After some researching and since I've heard of the reactive/asynchronous programming I stumbled upon
the `WebClient` provided by the `spring-boot-starter-webflux` library.

It is a reactive web client introduced in Spring 5. 
Moreover it's a non-blocking client and it belongs to the spring-webflux library,
the solution offers support for both synchronous and asynchronous operations, 
making it suitable also for applications running on a Servlet Stack.

This made my try it (I've never used reactive programming again, so thank you).
After that I configured the WebClient to get all the Pokemons using the `Mono` and `Flux` reactive streams.

This solved my problem and all the pokemons (around 1200) 
could be fetched for around 4 seconds (250 http request/second).

The PokeApi also has some issues where some Pokemons are not found (`404 NOT FOUND`) for an unknown reason.
The WebClient and the reactive streams provided with an elegant solution for this, where I just ignore the
Pokemons that could not be fetched, and I log each of those. 

This can be easily seen whenever the [PokeApiClient#getAllPokemons()](src/main/java/com/example/pokemon/service/PokeApiClient.java)
is called. The number of the Pokemons is bigger that the actual Pokemons gathered.

### Getting the Top 5 Red Version Pokemons
After gathering all the Pokemons I should provide with an interface to get the TOP 5 heaviest, highest, and experienced
pokemons.

#### In memory processing (no DB)
To get the top K Pokemons with in-memory processing is pretty easy.

The application uses Java 8 Streams to achieve this.

I've created this generic method that takes as input a `List<T>`, a `limit`, a `Predicate<T> filter`
and a`Comparator<T>` & a `boolean reversed` indicator to filter, sort nad and limit the number of elements in the list: 

```java
  private <T> List<T> getTopByComparatorAndFilter(List<T> list, int limit, Predicate<T> filter,
                                                  Comparator<T> comparator, boolean reversed) {
    return list.stream()
      .filter(filter)
      .sorted(reversed ? comparator.reversed() : comparator)
      .limit(limit)
      .collect(Collectors.toList());
  }
```

This makes the whole code very extendable and elegant
as one could pass and List of objects with any filter, sorting field, sorting order and a limit K
to get the `top K field-iest, elements (in ASC or DESC order) that qualify the filtering method`

This makes getting the TOP 5 Heaviest, Highest or Experienced Pokemons as simple as that:
```java
    public List<Pokemon> getTop5Heaviest() {
        return getTopByComparatorAndFilter(pokeApiClient.getAllPokemons(), 5, redVersionPokemons,
                Comparator.comparingInt(Pokemon::getWeight),true);
    }
```

#### Database Processing
First the app needed to store the pokemon data.

For that I've created an ER data model that has only the basic information around the Pokemons(primitive types)
as long as the list of GameIndex that need to be used in order to get the Red Versioned Pokemons.

I've used Hibernate and SpringData for that.
I've used `@ManyToMany` relationship for relating the `PokemonEntity` with their `GameIndexEntity`.

You can see more info around the ORM model here [PokemonEntity](src/main/java/com/example/pokemon/entity/PokemonEntity.java)
and here [GameIndexEntity](src/main/java/com/example/pokemon/entity/GameIndexEntity.java).

After that to fetch the top 5 Heaviest, Highest or Experienced Pokemons I used the SpringData JPARepository methods:

```java
@Repository
public interface PokemonRepository extends JpaRepository<PokemonEntity, Integer> {
    List<PokemonEntity> findByGameIndexEquals(GameIndexEntity gameIndex);

    List<PokemonEntity> findTop5ByGameIndexEqualsOrderByHeightDesc(GameIndexEntity gameIndex);

    List<PokemonEntity> findTop5ByGameIndexEqualsOrderByWeightDesc(GameIndexEntity gameIndex);

    List<PokemonEntity> findTop5ByGameIndexEqualsOrderByBaseExperienceDesc(GameIndexEntity gameIndex);
}
```

It's also possible to extend this to find any `Top K` pokemons but this will happen another time.


Caching Pokemons
-----
Since the Pokemons aren't frequently (I suppose), I've added a caching mechanism,
to cache the Pokemons instead of querying them (HTTP request for each Pokemons) each time
they needed to be fetched.

This is done by using the Caching Abstraction in Spring
which provides with an easy way to cache any data.

I've also added a scheduled that is completly configurable 
by the `pokemon.clearPokemonApiCache` environment variable that instruct how frequently
the Pokemons cache should be cleared.

In order to overwrite this use the command

```bash
    mvn spring-boot:run -Dspring-boot.run.arguments="--pokemon.clearPokemonApiCache=value"
```

Docker environment
----- 
The app is configured to produce a docker image that can be used to run the app directly.

There also exists a `docker-compose` environemnt (see [docker-compose.yml](docker-compose.yml))
that make this process even easier.

Testing
----- 
The app uses [Junit5](https://junit.org/junit5/docs/current/user-guide/) and [AssertJ](https://assertj.github.io/doc/)
and has a few simple test to test some of the behaviour (more will be added in the future).


