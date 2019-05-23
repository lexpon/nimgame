# NimGame

This is a simple implementation of a Nim game. The one, who pulls the last
stick, loses. You can interact with the game by sending REST requests and you
will receive REST responses with JSON payload. 

## Making the JAR
Run `mvn package spring-boot:repackage` to make the runnable jar. Afterwards it
can be found in the `target` folder (something like `target/nim-0.0.1-SNAPSHOT.jar`).

## Running the service
With this jar file you can run the service by using
```
java -jar target/<jarFileName>.jar
e.g.
java -jar target/nim-0.0.1-SNAPSHOT.jar
```

## API Documentation
To receive the API documentation, start the service and go to the following
URL:
```
<URL, e.g. "http://localhost:8080">/swagger-ui.html
```

## Configuring computer's strategy
By default the computer will pull a random number of sticks. So it has not a
real winning strategy by default. You can configure the behaviour by setting
the configuration in the `application.properties`. There are two possible
configurations:

* `computer.strategy=PULL_RANDOM`<br>
  This is the default behavior of the computer and does not have to be defined
  explicitly.
* `computer.strategy=PULL_TO_WIN`<br>
  The computer will choose the number of sticks in a way where it would win.
 