
# Maven 3.0.5-1
# Nano 2.2.6-1ubuntu1

FROM ubuntu:16.04

MAINTAINER PAGANINIST

# install wget
RUN apt-get update && apt-get install -y wget && apt-get clean

# set shell variables for java installation
ENV java_version 1.8.0_11
ENV filename jdk-8u11-linux-x64.tar.gz
ENV downloadlink http://download.oracle.com/otn-pub/java/jdk/8u11-b12/$filename

# download java, accepting the license agreement
RUN wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" -O /tmp/$filename $downloadlink 

# unpack java
RUN mkdir /opt/java-oracle && tar -zxf /tmp/$filename -C /opt/java-oracle/

# configure Java environment variables
ENV JAVA_HOME /opt/java-oracle/jdk$java_version
ENV PATH $JAVA_HOME/bin:$PATH

# configure symbolic links for the java and javac executables
RUN update-alternatives --install /usr/bin/java java $JAVA_HOME/bin/java 20000 && update-alternatives --install /usr/bin/javac javac $JAVA_HOME/bin/javac 20000

# install maven
RUN apt-get install -y maven
ENV MAVEN_HOME /usr/share/maven

# install git
RUN apt-get install -y git

# install nano
RUN apt-get install -y nano

# remove download archive files
RUN apt-get clean

# copy jenkins war file to the container
COPY ./target /target
#RUN chmod 644 /usr/target

# configure Jenkins environment variables

# configure the container to run jenkins, mapping container port 8080 to that host port
CMD ["ls", "/target"]
ENTRYPOINT ["java", "-Dloader.path=lib/", "-jar", "/target/videochat.war"]
EXPOSE 8080
EXPOSE 8443
EXPOSE 80
