#!/usr/bin/env bash
set -e

# --batch to prevent interactive command --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="${GPG_DECRYPT_PASSPHRASE}" --output ./.github/scripts/github.asc ./.github/scripts/github.enc

echo "Importing GPG key"

gpg2 --batch --passphrase ${GPG_PASSPHRASE} --import ./.github/scripts/github.asc