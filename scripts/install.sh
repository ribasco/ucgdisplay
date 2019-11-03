#!/usr/bin/env bash
set -e

echo "8888888 888b    888  .d8888b.  88888888888        d8888 888      888     "
echo "  888   8888b   888 d88P  Y88b     888           d88888 888      888     "
echo "  888   88888b  888 Y88b.          888          d88P888 888      888     "
echo "  888   888Y88b 888  \"Y888b.       888         d88P 888 888      888     "
echo "  888   888 Y88b888     \"Y88b.     888        d88P  888 888      888     "
echo "  888   888  Y88888       \"888     888       d88P   888 888      888     "
echo "  888   888   Y8888 Y88b  d88P     888      d8888888888 888      888     "
echo "8888888 888    Y888  \"Y8888P\"      888     d88P     888 88888888 88888888"

# Only cross-compile on linux (x86_64/amd64) environment

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    mvn --settings scripts/settings.xml install -P'cross-compile,!build-linux-x86_64' -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
else
    mvn --settings scripts/settings.xml install -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
fi