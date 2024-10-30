# Java Servlet Example

This example will cover building and deploying a simple servlet and web page using Docker.

## Building and running the application

```
$ docker build .

$ docker compose up
```

To visit your source go to:

```
http://localhost:8080/helloservlet/sayhello
```

## Deploying and running the application on OpenShift

```
$ oc new-project servlet
$ oc apply -n servlet -f manifests/my-servlet.yaml
$ oc expose -n servlet svc/hello
```
To visit your source go to:

```
http://[OpenShift Route location url]/helloservlet/sayhello
```
