# demofx
The code in this repository demonstrates how to build a JavaFX application. I use the application as a place for testing new facilities. The application is based on the following technologies:

* Java 11
* JsonB
* Server Sent Events
* JavaFX 17
* FXML
* Gluon Gradle plugins
* GraalVM
* Github workflows and actions

The code reduces the number of external dependencies to make it simpler to native compile the application. The only dependencies are:

* logback and slf4j for logging -- could be replaced with Java logging but I really like these tools
* johnzon-jsonb for handling Json serialization

I would have liked to use CDI, but the CDI implementations seem to be too huge (using millions of third party dependencies making native compilation difficult).

## What is demonstrated

* Use of Java, JavaFX, FXML to create a native application
* How to automatically update the application when new versions are released
* Some JavaFx facilities such as panes inside lists
* Use of Java 11 http client and JsonB for backend communication
* How to report issues from a client to be backend. An issue reports various information about the native client application such as version information, system properties/environment variables, screenshots, log files, etc.

## To come

* Server Sent Events for pushing events to the native application
* The backend services are implemented in Quarkus (lovely framework). I have not yet published these services but they will be made public.
* Use of OAuth/OIDC for authentication and authorization. This will come soon with Keycloak as OAuth server.
* Report exceptions back to the backend services.

## Contact

* Feel free to contact me.

## Links

* https://github.com/javieraviles/quarkus-github-flow demonstrates how to call an action when tagged.
* https://github.com/quarkiverse/quarkus-github-app demonstrations github app

