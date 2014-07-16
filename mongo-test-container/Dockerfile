FROM       ubuntu:latest

#MAINTAINER M.Y. Name <myname@addr.ess>

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
RUN echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | tee /etc/apt/sources.list.d/10gen.list

RUN apt-get update
RUN apt-get install -y -q mongodb-org

RUN mkdir -p /data/db

# Expose port 27017 from the container to the host
EXPOSE 27017


# Default port to execute the entrypoint (MongoDB)
CMD ["--port 27017"]




# Set usr/bin/mongod as the dockerized entry-point application
ENTRYPOINT usr/bin/mongod

