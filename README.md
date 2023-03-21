# demofx
The code in this repository demonstrates how to build a JavaFX application. I use the application as a place for testing new facilities. The application is based on the following technologies:

* Java 17
* JsonB
* Server Sent Events (not yet)
* JavaFX 19
* FXML
* OIDC by using Keycloak
* Gluon Gradle plugins
* GraalVM
* Github workflows and actions

The code reduces the number of external dependencies to make it simpler to native compile the application. The only dependencies are:

* logback and slf4j for logging -- could be replaced with Java logging but I really like these tools
* johnzon-jsonb for handling Json serialization

I would have liked to use CDI, but the CDI implementations seem to be too huge (using millions of third party dependencies making native compilation difficult). In general, I am trying to reduce the number of third party libraries in use like java-built-in httpclient (instead of other libraries). I simply need to be in controll of the source in use. 

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
* https://thrysoee.dk/keycloak-authorization-code-login/ nice description of pkce authorization flow
* https://auth0.com/docs/get-started/authentication-and-authorization-flow/call-your-api-using-the-authorization-code-flow-with-pkce#parameters also nice description of pkce authorization flow
* https://docs.oracle.com/en/java/javase/17/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpHandler.html the http webserver being used
* https://auth.stonemountain.dk/realms/demo/.well-known/openid-configuration look up urls on keycloak
* keycloak referneces:
** https://github.com/keycloak/keycloak/blob/923a321a55747d401d87b7958fe0dee81fabe010/common/src/main/java/org/keycloak/common/util/Base64Url.java (special base64 encoding)
** https://github.com/keycloak/keycloak/blob/923a321a55747d401d87b7958fe0dee81fabe010/core/src/main/java/org/keycloak/representations/AccessTokenResponse.java access token response
** https://github.com/keycloak/keycloak/blob/923a321a55747d401d87b7958fe0dee81fabe010/adapters/oidc/installed/src/main/java/org/keycloak/adapters/installed/KeycloakInstalled.java keycloak adapter pkce handling


