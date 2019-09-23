#!/usr/bin/env bash
set -e

echo "######   #######  #######  #######  ######   #######      ###  #     #   #####   #######     #     #        #      "
echo "#     #  #        #        #     #  #     #  #             #   ##    #  #     #     #       # #    #        #      "
echo "#     #  #        #        #     #  #     #  #             #   # #   #  #           #      #   #   #        #      "
echo "######   #####    #####    #     #  ######   #####         #   #  #  #   #####      #     #     #  #        #      "
echo "#     #  #        #        #     #  #   #    #             #   #   # #        #     #     #######  #        #      "
echo "#     #  #        #        #     #  #    #   #             #   #    ##  #     #     #     #     #  #        #      "
echo "######   #######  #        #######  #     #  #######      ###  #     #   #####      #     #     #  #######  #######"

if [[ $TRAVIS_OS_NAME == 'linux' ]] || [[ $TRAVIS_OS_NAME == 'osx' ]]; then
    which gcc && which g++
    gcc -v && g++ -v && cmake --version
    #ls -l /usr/bin/g*
    #ls -l /usr/bin/c*
fi

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    if [[ "$TRAVIS_PULL_REQUEST" == "false" ]]; then openssl aes-256-cbc -K $encrypted_0b32ef791498_key -iv $encrypted_0b32ef791498_iv -in scripts/travis.enc -out scripts/travis.dec -d; fi
    echo "Importing gpg secret key"
    gpg2 --batch --passphrase ${GPG_PASSPHRASE} --import scripts/travis.dec
else
    echo "Skipping gpg key import (OS = ${TRAVIS_OS_NAME})"
fi