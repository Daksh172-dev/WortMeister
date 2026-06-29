#!/usr/bin/env sh
set -eu

: "${MYSQL_HOST:=mysql}"
: "${MYSQL_PORT:=3306}"
: "${MYSQL_DATABASE:=wortmeister}"
: "${MYSQL_USER:=wortmeister}"
: "${MYSQL_PASSWORD:?MYSQL_PASSWORD is required}"
: "${BACKUP_FILE:?BACKUP_FILE is required}"

gzip -dc "$BACKUP_FILE" | mysql \
  --host="$MYSQL_HOST" \
  --port="$MYSQL_PORT" \
  --user="$MYSQL_USER" \
  --password="$MYSQL_PASSWORD" \
  "$MYSQL_DATABASE"
