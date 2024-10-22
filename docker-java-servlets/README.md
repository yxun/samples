# Java Servlet Example

This example will cover building and deploying a simple servlet and web page using Docker.

## Building and running the application

```
$ docker build .

$ docker compose up
```

Per default the application found in /src/helloservlet is served. You can change that by changing the APP_NAME and APP_CLASS in the docker-compose.yml.

To visit your source go to:

```
http://localhost:8080/helloservlet/sayhello
```
