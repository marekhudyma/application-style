# Application style 
In this application I demonstrate `oppinionated` style of writing backend application based on Java, Spring. 

## Integration Tests
Here I demonstrate how to use `Testcontainers` library to develop integration tests. 

# How to build the project 
To build the project execute (including running `integration tests` and `functional tests`)
```
mvn clean install
```

# How to run the application on localhost
```
java --enable-preview -jar target/application-style-exec.jar
```


# Links
[Zalando blog post](https://engineering.zalando.com/posts/2021/02/integration-tests-with-testcontainers.html)

[Personal blog post](https://marekhudyma.com/tests/2018/12/01/integration-tests-with-testcontainers.html)


# Concept of integration tests with testcontainers.org library
testcontainers.org  is a Java library that allows to run docker images and control them from Java code.  (I will not cover topic what is Docker, if you need more information <a href="https://en.wikipedia.org/wiki/Docker_(software)">read more</a> about it.)

The main concept of the proposal of the integration test is:
* Run your application
* Run ```external components``` as real docker containers. Here it is important to understand what I mean by ```external components```. It can be:
    * database storage - for example run real PostgreSQL as docker image,
    * Redis - run real Redis as docker image,
    * RabbitMQ
    * AWS components like S3, Kinesis, DynamoDB and others you can emulate by ```localstack```

**Don't run another microservice as docker image**. If you communicate with another microservice via HTTP, mock requests by ```mockserver``` run as docker images.

<figure>
  <img src="/assets/concept.jpg" alt="Concept"> 
  <figcaption>Your service comunicates with external components run as docker image. </figcaption>
</figure>


## Advantages

* You run tests against real components, for example H2 database doesn't support Postgres/MySQL specific functionality.
* You can run your tests offline - no Internet connection needed. It is an advantage for people who are traveling or if you have slow Internet connection.
* You can mock AWS services by ```localstack```. It will simplify administrative actions, cut costs and make your build offline.
* You can test cornercases like:
    * simulate timeout from external service,
    * simulate wrong http codes,
* All tests are written by developers in the same commit.

## Disadvantages
* Continuous integration (e.g. Jenkins) machine needs to be bigger (build uses more RAM and CPU).
* You need to run containers at least once - it consumes time and resources.

# How to run Integration Tests

To run PostgreSQL you can add it to your test:
```
protected static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.2")
        .withUsername("userName")
        .withPassword("password")
        .withDatabaseName("experimentDB");
```
Then run database migration scripts (eg. Flyway). Even empty test will validate if your migrations are executed properly.
As a good practice, you should remember about cleaning the state - delete inserted rows.

## Example of DB IntegrationTest
```
    @BeforeEach
    void setUp() throws Exception {
        name = UUID.randomUUID().toString();
    }
    @AfterEach
    void tearDown() throws Exception {
        accountRepository.findByName(name)
            .ifPresent(account -> accountRepository.delete(account));
    }
    @Test
    void shouldFindByName() throws Exception {
        Account account = Account.builder()
            .name(name).additionalInfo("additionalInfo").build();
        accountRepository.save(account);
    }
    Optional<Account> actual = accountRepository.findByName(name);
        assertThat(actual.get()).isEqualTo(account);
    }
```

# Disadvantages of testcontainer library
TestContainers support docker_compose file, but only version 2.0; while the docker_compose current version is 3.6.

# Alternative solutions
I liked TestContainers library. Personally I find `http://testcompose.com` library more interesting.
The main advantage is that it simply runs docker_compose file and reduces boilerplate code.
