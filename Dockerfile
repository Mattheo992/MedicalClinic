FROM openjdk:21-jdk
MAINTAINER Mattheo992
COPY target/medicalclinic-0.0.1-SNAPSHOT.jar MedicalClinic.jar
ENTRYPOINT ["java", "-jar", "MedicalClinic.jar"]