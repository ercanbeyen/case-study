# Case Study
---

## Spring Boot Application
---

### Summary

There is 1 document in this project.

Documents:
- Movie

```
POST   /api/v1/movies - Insert a new movie
GET    /api/v1/movies?type={type}&director={directorName}&rating={imdbRating}&orderBy={sortType}&maxSize={limit}&page={pageNumber}&size={pageSize} - Filter the movies with the specified criterias and list them on a page
GET    /api/v1/movies/search?title={movieTitle}&page={pageNumber}&size={pageSize} - Search movies by movie title and list the final values on a page
GET    /api/v1/movies/pagination?page={pageNumber}&size={pageSize} - List the movies on a page
GET    /api/v1/movies/{id} - Fetches a particular movie
PUT    /api/v1/movies/{id} - Updates a particular movie
DELETE /api/v1/movies/{id} - Deletes a particular movie
POST   /api/v1/movies/restore/database - Restore the database using the records from the MovieWebSiteJson document
```

### Requirements

- Each movie must have imdb id, title, released year, director, writers and actors and type
- Each imdb id must be unique.
- Metascore, imdb rating, imdb votes and total seasons must positive value
- You may also enter the value of total seasons which type is series

### Tech Stack

- Java 8
- Spring Boot
- Spring Data MongoDB
- JUnit 5
- MongoDB
- Scheduler
- OpenAPI Documentation
- Docker
- Docker Compose


### Prerequisties
---
- Maven
- Docker

### Run & Build
---

In order to pull the mongo image from the Dockerhub, you should run the below command

`$ docker pull mongo`

Then, you should run the below commands in order to run the aoolication

1) Create jar file
2) Create the image of the application via building
3) Run the containers

```
$ mvn clean install
$ docker-compose build
$ docker-compose up
```

### Api Documentation
---

You may use swagger-ui with the port of the application to access the project's api documentation.<br/>
`http://localhost:${PORT}/swagger-ui.html`
