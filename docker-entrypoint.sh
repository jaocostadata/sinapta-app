#!/bin/sh
set -e

if [ -n "$DB_HOST" ]; then
  export SINAPTA_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT:-5432}/${DB_NAME}"
  export SINAPTA_DB_USER="$DB_USER"
  export SINAPTA_DB_PASSWORD="$DB_PASSWORD"
fi

exec java -jar /app/app.jar --spring.profiles.active="${SPRING_PROFILES_ACTIVE:-prod}"
