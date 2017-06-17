#!/bin/bash

# check if required tools installed
command -v newman >/dev/null 2>&1 || { echo >&2 "Newman has to be installed. Aborting."; exit 1; }

POSTMAN_ENV=${1:-"Local"}

rm -rf newman

newman run Videostat.postman_collection.json -e ${POSTMAN_ENV}.postman_environment -n 1 --delay-request 300 -r html,cli,junit
