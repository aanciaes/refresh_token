# Refresh Tokens

An example of an implementation model of the refresh tokens security architecture.

A simple example when performing operations or accessing resources with different permissions.  
The refresh token TTL is set to 1 min, so it's easier to test the expiration date and the re-authentication process that is needed when the token expires.

Backend supplied by Spotify Apollo Http-service, both in kotlin and in java implementations

The backend simulates a resource server and an authentication server, in this case, all in one but can be separated 

Small simplistic front-end, served by IntelliJ server in http://localhost:63342. Can be changed.

### Default users

username: miguel  
password: miguelpassword  
Admin

username: joao  
password: joaopassword  
Basic user



