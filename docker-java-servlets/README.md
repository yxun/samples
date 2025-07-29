# Java Servlet Example

This example will cover building and deploying a simple servlet and web page using Docker.

## Building and running the application

```
$ docker build .

$ docker compose up
```

To visit your source go to:

```
http://localhost:8080/myservlet/login
```

## Deploying and running the application on OpenShift

```
$ oc new-project servlet
$ oc apply -n servlet -f manifests/my-servlet.yaml
$ oc apply -n servlet -f manifests/my-servlet-gateway.yaml
```
To visit your source go to:

```
http://[OpenShift Route location url]/myservlet/login
http://[OpenShift Route location url]/myservlet/role
```
