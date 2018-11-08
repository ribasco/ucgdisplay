#!/usr/bin/env bash
set -e

echo '--------------------------------------------------------------------------'
echo ' '
echo '8""""8                            8""""8                                  '
echo '8    " eeeee  eeeee eeeee eeeee   8    " eeeee eeeeeee eeeee e  e     eeee'
echo '8e     8   8  8  88 8   " 8   "   8e     8  88 8  8  8 8   8 8  8     8   '
echo '88     8eee8e 8   8 8eeee 8eeee   88     8   8 8e 8  8 8eee8 8e 8e    8eee'
echo '88   e 88   8 8   8    88    88   88   e 8   8 88 8  8 88    88 88    88  '
echo '88eee8 88   8 8eee8 8ee88 8ee88   88eee8 8eee8 88 8  8 88    88 88eee 88ee'
echo ' '
echo '--------------------------------------------------------------------------'

cd ..

mvn clean compile -P'cross-compile,!build-linux-amd64'