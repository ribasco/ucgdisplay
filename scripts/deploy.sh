#!/bin/bash
set -e # Exit with nonzero exit code if anything fails

# Do not deploy on pull-requests or commits on other branches
if [ "${TRAVIS_PULL_REQUEST}" != "false" -o "${TRAVIS_BRANCH}" != "master" ]; then
    echo "Skipping deploy (Reason: Pull-Request or Non-master branch detected)"
    exit 0
fi

if [[ $TRAVIS_OS_NAME == 'linux' ]] && [[ ${TRAVIS_PULL_REQUEST} == 'false' ]] && [[ ${TRAVIS_TAG} == '' ]]; then
    echo " _____   ______  _____    _         _____    _     _ "
    echo "(_____) (______)(_____)  (_)       (_____)  (_)   (_)"
    echo "(_)  (_)(_)__   (_)__(_) (_)      (_)   (_)  (_)_(_) "
    echo "(_)  (_)(____)  (_____)  (_)      (_)   (_)    (_)   "
    echo "(_)__(_)(_)____ (_)      (_)____  (_)___(_)    (_)   "
    echo "(_____) (______)(_)      (______)  (_____)     (_)   "
    echo "                                                     "

    mvn deploy -DskipTests --settings scripts/settings.xml
fi