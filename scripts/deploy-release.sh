#!/usr/bin/env bash

if [[ "$TRAVIS_PULL_REQUEST" == "false" ]]; then

  echo -e "Running release script...\n"
  cd $HOME

  rsync -r --quiet -e "ssh -p 2222 -o StrictHostKeyChecking=no" $HOME/build/internetpolice-eu/Launcher/target/mvn-repo/ \
  travis@travis.internetpolice.eu:WWW/repo/

  echo -e "Publishing final plugin release...\n"

  rsync -r --quiet -e "ssh -p 2222 -o StrictHostKeyChecking=no" $HOME/build/internetpolice-eu/Launcher/target/Launcher-*.jar \
  travis@travis.internetpolice.eu:WWW/downloads/Launcher/

fi
