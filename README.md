# akka-http-rest [![Build Status](https://travis-ci.org/JauFeng/akka-http-rest.svg?branch=master)]((https://travis-ci.org/JauFeng/akka-http-rest.svg))
A RESTful API for my **[App](https://github.com/JauFeng/MyApplication)**. In addition to this, you can use it as a microservice architecture.
## Prerequirement
I assume that you have installed Mongodb(3.2) already. And created a database named `dev`, a collection named `patient`. Of course, you can use ***[Flyway](https://flywaydb.org)*** to evolve your Database Schema easily.
## Running
Start the services with sbt:
```
 $ sbt api/run
```
## Testing
Testing with sbt:
```
> sbt test
```
## Docker
Create a new Docker image`api:1.0-SNAPSHOT`:
```
> sbt docker:publishLocal
```
and then view the images user `docker images` to check it out:
```
$ docker images
```
Start a container by the `api:1.0-SNAPSHOT` image use command:
```
$ docker run --name api-8080 -p 8080:9000 api:1.0-SNAPSHOT
```

See more: *[Docker](https://www.docker.com/)*