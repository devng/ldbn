# Docker file for running the LDBN application using NginX

FROM ubuntu:trusty

# Let the conatiner know that there is no tty
ENV DEBIAN_FRONTEND noninteractive

# Update base image
RUN apt-get update && apt-get -y dist-upgrade

# Install software requirements
RUN apt-get -y install nginx sqlite3 php5-fpm php5-sqlite

# Add nginx site conf, start script and ldbn www data
ADD docker/nginx.conf /etc/nginx/
ADD docker/start.sh /
ADD build/www /var/ldbn/www

# Add a shared volume for the sqlite3 db
VOLUME ["/var/ldbn/sql"]

# Add the root user to the www-data group
RUN adduser root www-data

# Expose Ports
EXPOSE 80

ENTRYPOINT ["/start.sh"]
