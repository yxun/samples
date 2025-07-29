FROM tomcat:9.0.96-jdk8-corretto-al2

WORKDIR /usr/local/tomcat/bin
COPY run.sh run.sh
RUN chmod +x run.sh

ENV JPDA_ADDRESS="8000"
ENV JPDA_TRANSPORT="dt_socket"

COPY ./src /usr/local/tomcat/webapps
WORKDIR /usr/local/tomcat/webapps/myservlet/WEB-INF
RUN javac -classpath .:/usr/local/tomcat/lib/servlet-api.jar -d classes src/mypkg/*

WORKDIR /usr/local/tomcat/bin
