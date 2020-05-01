# Docker file for running the LDBN application using NginX

FROM nginx:1.17

# Update base image
RUN apt-get update

# Install software requirements
RUN apt-get install -y php7.3-fpm php7.3-sqlite3

RUN apt-get autoremove && apt-get clean

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
