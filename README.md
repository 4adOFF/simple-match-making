# Info
Simple match making service based on [K-mean algorithm](https://www.baeldung.com/java-k-means-clustering-algorithm)

We have one Post request for save User entity
```
public class User {
    String name;
    Double skill;
    Double latency;
    Instant requestTime;
}
```

On save every `User` trying to match group of players.

In ``application.yml`` you can configure <br> <br> ``maxIterations`` count of iteration for K-mean algorithm and <br> ``groupSize`` for size matching group