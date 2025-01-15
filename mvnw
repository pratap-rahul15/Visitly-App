#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements. See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# ----------------------------------------------------------------------------

set -euf
[ "${MVNW_VERBOSE-}" != debug ] || set -x

# OS-specific support
native_path() { printf %s\\n "$1"; }
case "$(uname)" in
CYGWIN* | MINGW*)
  [ -z "${JAVA_HOME-}" ] || JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
  native_path() { cygpath --path --windows "$1"; }
  ;;
esac

# Function to set JAVA_HOME
set_java_home() {
  if [ -n "${JAVA_HOME-}" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
    JAVACCMD="$JAVA_HOME/bin/javac"
    if [ ! -x "$JAVACMD" ] || [ ! -x "$JAVACCMD" ]; then
      echo "The JAVA_HOME environment variable is not defined correctly." >&2
      return 1
    fi
  else
    JAVACMD="$(command -v java)" || :
    JAVACCMD="$(command -v javac)" || :
    if [ ! -x "${JAVACMD-}" ] || [ ! -x "${JAVACCMD-}" ]; then
      echo "The java/javac command does not exist in PATH nor is JAVA_HOME set." >&2
      return 1
    fi
  fi
}

# Improved quoting for variables with potential spaces
while IFS="=" read -r key value; do
  case "${key-}" in
  distributionUrl) distributionUrl="$(trim "${value-}")" ;;
  distributionSha256Sum) distributionSha256Sum="$(trim "${value-}")" ;;
  esac
done < "${0%/*}/.mvn/wrapper/maven-wrapper.properties"

[ -n "${distributionUrl-}" ] || die "Cannot read distributionUrl property."

# Proper quoting of paths
MAVEN_USER_HOME="${MAVEN_USER_HOME:-${HOME}/.m2}"
MAVEN_HOME="${MAVEN_USER_HOME}/wrapper/dists/${distributionUrl##*/}/$(hash_string "$distributionUrl")"

# Executing Maven with proper quotes
exec_maven() {
  unset MVNW_VERBOSE MVNW_USERNAME MVNW_PASSWORD MVNW_REPOURL || :
  exec "$MAVEN_HOME/bin/mvn" "$@" || die "Cannot execute Maven from $MAVEN_HOME."
}

if [ -d "$MAVEN_HOME" ]; then
  verbose "Found existing Maven installation at \"$MAVEN_HOME\""
  exec_maven "$@"
fi

# Handle spaces in temp directory
if TMP_DOWNLOAD_DIR="$(mktemp -d)" && [ -d "$TMP_DOWNLOAD_DIR" ]; then
  clean() { rm -rf -- "$TMP_DOWNLOAD_DIR"; }
  trap clean HUP INT TERM EXIT
else
  die "Cannot create a temporary directory."
fi

mkdir -p -- "${MAVEN_HOME%/*}"

# Download Maven with quoted paths
if command -v wget >/dev/null; then
  wget "$distributionUrl" -O "$TMP_DOWNLOAD_DIR/${distributionUrl##*/}" || die "Failed to fetch Maven."
elif command -v curl >/dev/null; then
  curl -f -L -o "$TMP_DOWNLOAD_DIR/${distributionUrl##*/}" "$distributionUrl" || die "Failed to fetch Maven."
else
  set_java_home
  "$JAVACMD" -cp "$(native_path "$TMP_DOWNLOAD_DIR")" Downloader "$distributionUrl" "$(native_path "$TMP_DOWNLOAD_DIR/${distributionUrl##*/}")"
fi

# Proper unzipping with quoted paths
if command -v unzip >/dev/null; then
  unzip "$TMP_DOWNLOAD_DIR/${distributionUrl##*/}" -d "$TMP_DOWNLOAD_DIR" || die "Failed to unzip."
else
  tar xzf "$TMP_DOWNLOAD_DIR/${distributionUrl##*/}" -C "$TMP_DOWNLOAD_DIR" || die "Failed to untar."
fi

mv -- "$TMP_DOWNLOAD_DIR/${distributionUrlNameMain}" "$MAVEN_HOME" || die "Failed to move Maven."
exec_maven "$@"
