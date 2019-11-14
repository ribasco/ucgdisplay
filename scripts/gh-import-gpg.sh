#!/usr/bin/env bash

# --batch to prevent interactive command --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="${GPG_DECRYPT_PASSPHRASE}" --output scripts/github.asc scripts/github.enc

echo "Importing GPG key"

gpg2 --batch --passphrase ${GPG_PASSPHRASE} --import scripts/github.asc