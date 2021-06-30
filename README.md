# identity-fort

Lightweight Identity Provider built using Spring Boot and pring Security oAuth.
User can register and create clients, then 3rd party app can authenticate
against this app.

### Configuration
Database configuration resides in `src/main/resources/application.properties`.
`schema.sql` and `data.sql` are run for creating necessary tables and default data.

### Run Server
It is a standard Spring Boot application with `gradle` as build tool.
**Postgres** database and local development mail server **mailhog** is
configured in `docker-compose.yml`.

Following commands will start `postgres` and `mailhog`:

```bash
$ docker-compose up -d
```

After that executing following `gradle` command will make the app available at `http://localhost:8080`:

```bash
$ ./gradlew clean build bootRun
```

### 
Default user with email: `john@example.com`, password: `amdin` and oauth client
with clientId: `test-client`, client secret: `admin`, redirect_uri:
`http://localhost/callback` is created.

When following url is visited from browser, app will redirect to login page.
After entering valid credentials it will again redirect to
'http://localhost/callback' with a `code`.

```text
http://localhost:8080/oauth/authorize?response_type=code
&client_id=test_client
&redirect_uri=http%3A%2F%2Flocalhost%2Fcallback
&scope=user_info
&state=1
```
With this `code`, acquire access token from `http://localhost:8080/oauth/token`:

```shell
curl --user test_client:admin \
            -d 'grant_type=authorization_code&code=<CODE>&redirect_uri=http://localhost/callback' \
            http://localhost:8080/oauth/token

{
   "access_token":"f130c3ed-6407-4fca-bb52-dc6019921247",
   "token_type":"bearer",
   "refresh_token":"d03e09ab-44c0-4914-8c6b-fc519522dc0e",
   "expires_in":3599,
   "scope":"user_info"
}
```

With this `access_token` user information can be fetching from `http://localhost:8080/v1/users/info` endpoint:
```bash
curl -H 'Authorization: Bearer f130c3ed-6407-4fca-bb52-dc6019921247' http://localhost:8080/v1/users/info

{
   "id":1,
   "firstName":"John",
   "lastName":"Doe",
   "registrationDate":"2021-06-30T09:43:07.340+0000",
   "email":"john@example.com",
   "address":null,
   "enabled":true,
   "locked":false,
   "username":"john@example.com"
}
```