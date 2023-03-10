Voting system for deciding where to have lunch.

[local REST API documentation](http://localhost:8889/open-api)

[remote REST API documentation](http://62.113.255.104:8889/open-api)

` Note - remote application time zone is GMT+3 and all data is reset daily at midnight`

By default, users after registration has no USER role, activate "REGISTER_ROLE_SET" profile to set role during
registration.
To restrict modification of initially created users, activate "VDS" profile.

<details>
	<summary> API overview </summary>

### Guest

#### Registration

POST `http://localhost:8889/api/register` - register new user. Depending on current app profile after registration new
user may not have USER role set, that user will be unable to use any non-profile API endpoints without manually setting
role by admin

#### Profile

GET `http://localhost:8889/api/profile` - get logged user info

PUT `http://localhost:8889/api/profile` - update logged user info

DELETE `http://localhost:8889/api/profile` - delete logged user

### User

All user endpoints requires USER role

#### Restaurants

GET `http://localhost:8889/api/restaurants/menus/{date}` - get all restaurants with menu of the day for provided date

#### Voting

GET `http://localhost:8889/api/profile/votes/{date}` - get logged user vote

PUT `http://localhost:8889/api/profile/votes/{date}` - vote for restaurant

GET `http://localhost:8889/api/restaurants/votes/{date}` - get voting result for date, if provided date is today voting
results may not be final

### Admin

All admin endpoints requires ADMIN role

#### Users

GET `http://localhost:8889/api/admin/users/{id}` - get user info

PUT `http://localhost:8889/api/admin/users/{id}` - update user info

DELETE `http://localhost:8889/api/admin/users/{id}` - delete user

PATCH `http://localhost:8889/api/admin/users/{id}` - disable/enable user

GET `http://localhost:8889/api/admin/users` - get all users info

POST `http://localhost:8889/api/admin/users` - create user

#### Restaurants

GET `http://localhost:8889/api/admin/restaurants/{id}` - get restaurant info

PUT `http://localhost:8889/api/admin/restaurants/{id}` - update restaurant info

DELETE `http://localhost:8889/api/admin/restaurants/{id}` - delete restaurant

GET `http://localhost:8889/api/admin/restaurants` - get all restaurants info

POST `http://localhost:8889/api/admin/restaurants` - create new restaurant

#### Menus

GET `http://localhost:8889/api/admin/restaurants/{id}/menus/{date}` - get restaurants menu of the day

PUT `http://localhost:8889/api/admin/restaurants/{id}/menus/{date}` - update restaurants menu of the day

DELETE `http://localhost:8889/api/admin/restaurants/{id}/menus/{date}` - delete restaurants menu of the day

</details>

### Stack

JDK 17, Spring Boot 3.0, Lombok, H2, Caffeine Cache, Swagger/OpenAPI 3.0

### Run

Requirements: JDK 17+ , Maven

- Run from maven: `mvn spring-boot:run` in project root directory. With
  profiles: `mvn spring-boot:run -Dspring-boot.run.profiles=VDS,REGISTER_ROLE_SET`
- Build and run: `mvn package` in project root directory and then `java -jar ./target/VotingSystem.jar`,
  or `java -Dspring-boot.run.profiles=VDS,REGISTER_ROLE_SET ./target/VotingSystem.jar`

## Original task ##

Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and
couple curl commands to test it (**better - link to Swagger**).