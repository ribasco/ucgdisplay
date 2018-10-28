#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Do not deploy on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy (Reason: Pull-Request or Non-master branch detected)"
    exit 0
fi

echo "============================================="
echo "Deploying Project to OSS Sonatype"
echo "============================================="

[[ ${TRAVIS_PULL_REQUEST} == 'false' ]] && [[ ${TRAVIS_TAG} == '' ]] && mvn deploy -DskipTests --settings scripts/settings.xml