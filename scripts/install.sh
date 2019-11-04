#!/usr/bin/env bash
set -e

echo '  _____ _   _  _____ _______       _      _      '
echo ' |_   _| \ | |/ ____|__   __|/\   | |    | |     '
echo '   | | |  \| | (___    | |  /  \  | |    | |     '
echo '   | | | . ` |\___ \   | | / /\ \ | |    | |     '
echo '  _| |_| |\  |____) |  | |/ ____ \| |____| |____ '
echo ' |_____|_| \_|_____/   |_/_/    \_\______|______|'
echo '                                                 '

# Only cross-compile on linux (x86_64/amd64) environment

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    mvn --settings scripts/settings.xml install -P'cross-compile,!build-linux-x86_64' -Dlicense.skipUpdateLicense=true -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
else
    mvn --settings scripts/settings.xml install -DskipTests=true -Dgpg.skip -Dmaven.javadoc.skip=true -B -V
fi