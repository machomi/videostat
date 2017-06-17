# Api tests using POSTMAN

This folder contains postman collection files and definitions of specific environments. 
You can import them using POSTMAN GUI interface or use command line tool *Newman*.

# Collections:

## Videostat

Steps: 
 1. Login as videouser
 2. Post video file (happy path).
 3. Post empty file.
 4. Post proper file but without authentication tokent.



## Installing Newman

    npm install -g newman

## Runing newman test from command line

In order to run newman collection test use command:

    new run collection_file -e environment_file -d data_file -n number_of_iterations --delay-request time_between_requests

Options -d, -n, --delay are optional.

Example:

    newman run Videostat.postman_collection.json -e Google.postman_environment -n 1 --delay-request 300
