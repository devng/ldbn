#!/bin/bash

service php7.3-fpm start && /usr/sbin/nginx -g "daemon off;" "$@"
