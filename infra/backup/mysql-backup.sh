#!/usr/bin/env sh
set -eu

: "${MYSQL_HOST:=mysql}"
: "${MYSQL_PORT:=3306}"
: "${MYSQL_DATABASE:=wortmeister}"
: "${MYSQL_USER:=wortmeister}"
: "${MYSQL_PASSWORD:?MYSQL_PASSWORD is required}"
: "${BACKUP_DIR:=/backups}"

timestamp="$(date -u +%Y%m%dT%H%M%SZ)"
mkdir -p "$BACKUP_DIR"

mysqldump \
  --host="$MYSQL_HOST" \
  --port="$MYSQL_PORT" \
  --user="$MYSQL_USER" \
  --password="$MYSQL_PASSWORD" \
  --single-transaction \
  --routines \
  --triggers \
  "$MYSQL_DATABASE" | gzip > "$BACKUP_DIR/wortmeister-$timestamp.sql.gz"

find "$BACKUP_DIR" -type f -name "wortmeister-*.sql.gz" -mtime +14 -delete
