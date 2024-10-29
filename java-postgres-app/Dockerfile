FROM openjdk:11-slim

RUN apt-get update && apt-get install -y postgresql-client

# Create a non-root user and group and set permissions
RUN useradd -ms /bin/bash appuser
USER appuser

WORKDIR /app

COPY . /app

# Include the JDBC driver in the classpath
RUN javac -cp postgresql-42.2.20.jar Main.java

CMD ["java", "-cp", ".:postgresql-42.2.20.jar", "Main"]