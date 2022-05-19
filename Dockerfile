FROM amazoncorretto:11.0.15
RUN yum -y install python3 \
        python3-pip \
        shadow-utils && \
        adduser --system spring
USER spring
WORKDIR /home/spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /home/spring/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/home/spring/app.jar"]
#docker run -p 8080:8080 finance SPRING_PROFILES_ACTIVE=prod