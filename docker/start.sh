#!/bin/bash

service php5-fpm start && /usr/sbin/nginx -g "daemon off;" "$@"
