# PKCE
Startup order

```
mvn spring-boot:run
```

* spa-example
* discovery_service
* api_gateway
* demo_resource_server
    * You can start multiple instances
    * API Gateway knows all instances (discover service) and will load balance the traffic
