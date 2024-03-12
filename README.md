### Running instruction:
1. Run the service:

* Spring Boot: 

``./gradlew bootRun``

* Docker: 

`docker-compose up`

2. Test:
* Run unit and integration test:

`./gradlew test`

* Test with http call:

[api.http](api.http)

### What has been done:

* Api allows to manage promotion campaign. A user can create, read and delete promotion campaign providing a product count ranges and percentage discount. 
In case when range of product count (from - to) is not provided one percentage discount regardless of its count is applied. 
* Api used to view existing products and to allocate given products providing its uuid and number of items.

### Assumptions
* Current implementation allows to create a global campaign for all existing products for simplicity. Ideally the production campaign should be targeted to given products.
* The api dedicated to manage campaign suppose to be a part of an admin api to allow only authorised users to manage it.
* For simplicity all the data is kept in memory although in real live I would persist it in a relational database.
* Once implementing a task I prioritized the matter of this task which was: **calculating a given product&#39;s price based on the number of
  products ordered**. In real world I would add much more validation and for campaign api and improve test coverage.
