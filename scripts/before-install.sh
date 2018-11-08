#!/usr/bin/env bash
set -e

echo "######   #######  #######  #######  ######   #######      ###  #     #   #####   #######     #     #        #      "
echo "#     #  #        #        #     #  #     #  #             #   ##    #  #     #     #       # #    #        #      "
echo "#     #  #        #        #     #  #     #  #             #   # #   #  #           #      #   #   #        #      "
echo "######   #####    #####    #     #  ######   #####         #   #  #  #   #####      #     #     #  #        #      "
echo "#     #  #        #        #     #  #   #    #             #   #   # #        #     #     #######  #        #      "
echo "#     #  #        #        #     #  #    #   #             #   #    ##  #     #     #     #     #  #        #      "
echo "######   #######  #        #######  #     #  #######      ###  #     #   #####      #     #     #  #######  #######"


if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
    sudo ln -s /usr/bin/gcc-7 /usr/local/bin/gcc
    sudo ln -s /usr/bin/g++-7 /usr/local/bin/g++
    gcc -v && g++ -v && cmake --version
    export CC=/usr/bin/gcc-7
    export CXX=/usr/bin/g++-7
    if [[ "$TRAVIS_PULL_REQUEST" == "false" ]]; then openssl aes-256-cbc -K $encrypted_0b32ef791498_key -iv $encrypted_0b32ef791498_iv -in scripts/travis.enc -out scripts/travis.dec -d; fi
    gpg2 --import scripts/travis.dec
else
    echo "Skipping before install phase (OS = ${TRAVIS_OS_NAME}"
fi