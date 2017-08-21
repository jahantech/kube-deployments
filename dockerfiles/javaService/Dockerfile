FROM java:latest
MAINTAINER jahansyed@hotmail.co.uk
RUN mkdir -p /usr/app/javaservice
COPY . /usr/app/javaservice/
RUN chmod a+x /usr/app/javaservice/run-script
WORKDIR /usr/app/javaservice
EXPOSE 8000
ENTRYPOINT ["./run-script"]
