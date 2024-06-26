FROM --platform=amd64 gradle:7.6.4-jdk11

COPY . /code/
WORKDIR /code
RUN gradle clean bootJar

FROM --platform=amd64 openjdk:11

COPY --from=0 /code/build/libs/rqChallenge-0.0.1-SNAPSHOT.jar /app/rqChallenge.jar
CMD java -jar /app/rqChallenge.jar
