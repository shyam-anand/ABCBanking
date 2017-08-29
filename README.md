# ABCBank
A Spring Project with ReST, JPA, MySQL, Redis and Spring Security

### Dependencies
The project requires Java 8 and Maven to build, and connections to MySQL and Redis.

### Running the project

1. Configure MySQL and Redis connections using `src/resources/application.properties`. Use the template file to see the parameters required.
2. Run `mvn spring-boot:run` in the root directory.
3. You could also run `mvn install` to create a deployable `.war` file.

The project will create the tables, will insert sample data into the services table and create the default manager user.

### Usage

1. Create counters for the services

Services are listed at `/services` endpoint. Add a counter using `POST` requests to `/counters?service={serviceId}`. Counter can be set as PRIORITY using 
the optional parameter `priority` with value `true`.

2. Create customers using POST requests to `/customer` endpoint. The customer details should be in the request body, as show below.

    {
        "name": "[customer-name]",
        "phone": "[phone-number]",
        "address": "[address]",
        "type": "[NORMAL/PRIORITY]",
        "user_name": "[unique-username]",
        "password": "[password]"
    }
    
2. Create a service request to generate token

Using `POST` to `/services/{serviceId}/request?customerId={customerId}`.

This will generate a token, and add the token to the queue for the relevant service counter.
If a token has already been generated of the same user for the same service, the token will be returned as it is. If there's a token for the user, but
not for this service, this service will be added to the token.

3. Serve the customers

At `/counters/{counterId}/serve` (`PUT` request).

This endpoint will set the token at the top of the queue as PROCESSING, and will return the details.
 
4. Advance to the next token

Using `/counters/{counterId}/next`.

This will move the token which was being served to the next counter for that token, or will set it as COMPLETED if no more counters are to be visited.
It will then return the next token in the queue for the counter.

### Endpoints

All the endpoints are listed under `/swagger-ui.html`.

Most endpoints needs Basic Authentication. A default user is created with full privileges as the application is started. The credentials are 
Username: manager
Password: manager

**A few useful endpoints**

`/counters/{counterId}/queue` - View the token queue for a counter
`/tokens/{tokenId}` - GET to view details, PUT or POST with parameter `status` to update the status.
`/tokens/{tokenId}/action` - POST to create an Action for the Token

#### Other notes

The application log will show the operations being done with proper details. Viewing the logs should help in understanding the logic and flow.

