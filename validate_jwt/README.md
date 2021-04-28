
Validate JSON Web Tokens (JWT) using Auth0 lib.  
Controller is secured by the Filter.

### Run
First start Keycloak server and create 2 realms.  
Set keycloak host:port on application.yml.
Use postman to test API's

```
auth
http://localhost:8080/auth/realms/<tenant>/protocol/openid-connect/token


api
http://localhost:8081/api/tenant/<tenant>/hello
```