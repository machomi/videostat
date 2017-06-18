# Videostat

Videostat is a simplistic application which investigates video file and returns some video metadata. 

Application name refers to some similarity with unix *stat* function.

[![CircleCI](https://circleci.com/gh/machomic/videostat.svg?style=svg)](https://circleci.com/gh/machomic/videostat)

## Caveats

Application requires FFmpeg library to be installed on the host system. To install it go to: https://ffmpeg.org/ and follow instructions provided there.

Upload file size is limited to 25MB.

## Configuration 

There is a configuration file _application.yml_ which contains some custom configuration.

Available options:

**ffprobe-path** - defines path to ffprobe which is a part of ffmpeg library.

**supported-formats** - list of video mime types which application should support.

Example configuration:

```
videostat:
  ffprobe-path: ffprobe
  supported-formats:
    - 'video/x-flv'
    - 'video/mp4'
    - 'application/x-mpegURL'
    - 'video/MP2T'
    - 'video/3gpp'
    - 'video/quicktime'
    - 'video/x-msvideo'
    - 'video/x-ms-wmv'
```

These configuration options can be easily overridden by run options:

`java -jar app.jar -Dvideostat_ffprobe_path=/usr/bin/ffprobe -Dvideostat_supported_formats="video/x-flv,video/mp4"`

or by environment variables:

```
export VIDEOSTAT_FFPROBE_PATH=/usr/bin/ffprobe
export VIDEOSTAT_SUPPORTED_FORMATS="video/3gpp,video-mpeg"
java -jar app.jar
``` 
 

## Development

Application is prepared using maven and distributed as spring boot jar (tomcat embedded). Most IDEs will recognize and build it automatically. 

Maven wrapper is also included so you would not have install maven on your host.

To simply run it from command line just type `./mvnw`

Please bear in mind that ffmpeg command must be accessible to work it properly.

## Running as docker container

If you don't want to run it from source code there is an option to easily run it as docker container. To do so please type:

`docker pull mmachowski/videostat`

`docker run -p 8080:8080 videostat` 

Now application will be available at http://you_docker_host:8080. 

## Exposed endpoints

First you need to authenticate user and get JWT access token:

```
curl -X POST \
  http://localhost:8080/oauth/token \
  -H 'authorization: Basic dmlkZW9jbGllbnQ6' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d 'username=videouser&password=p%40ssw0rd&grant_type=password'
```
  
Please copy access token from json response and use it in following request as authorization header value.

---

Apart from authentication there is only one pretty useful POST endpoint: **http://localhost:8080/video/stat**

Example curl command to use it:

```
curl -X POST \
  http://localhost:8080/video/stat \
  -H 'accept: application/json' \
  -H 'authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTc3NjIwMDgsInVzZXJfbmFtZSI6InZpZGVvdXNlciIsImF1dGhvcml0aWVzIjpbInZpZGVvIl0sImp0aSI6IjNhZGU1ZmRlLWNkMDgtNDFlMy05Y2YwLTMyYmU1MzVkODc5NiIsImNsaWVudF9pZCI6InZpZGVvY2xpZW50Iiwic2NvcGUiOlsidmlkZW8iXX0._erRssbJTmkYRDv42i206cUmXnxnTZd8eVKkLx9ZCsI' \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data' \
  -F 'video=@sample.mp4;type=video/mp4'
```

## Authentication

For the purpose of this demo project application supports OAuth2 JWT authentication process. However there is no specific database yet.

In memory credentials are:

* username: **videouser**
* password: **p@ssw0rd**
* clientId: **videoclient** 

## Testing

There are pretty extensive unit and integration test to this project. Considering project size integration tests are not extracted to seperate test profile. 

Command `mvn integration-test` will take care of all project test except api test. 

## Dockerization

In order to build docker image use command:

`mvn clean package docker:build`

This will build the app and wrap it into docker image. Image already contains java and ffmpeg library.

To check if image is in your local storage type:

`docker images`

You should be able to find image with name videostat.

To run your image as local container type:

`docker run -p 8080:8080 videostat` 

Application will be available at http://localhost:8080 unless you have your docker host exposed with different ip. 

## Continues integration and continues deployment

For purpose of CI and CD we use CircleCI service which is quite convinient especially with service dockerization.

Current process includes steps:

* fetching code from github repo
* installing ffmpeg on vm (for puprpose of integration test)
* installing gcloud tools and configuring access to google cloud project
* building application jar
* running integration test
* building docker image
* publishing image in docker hub registry
* deploying application into google cloud container engine (using kubernates cluster)

Configuration is defined in _circle.yml_ file.

## Api Testing

There are api tests prepared using POSTMAN GUI and NEWMAN command tools. You can find them under **src/test/api** folder. There is also dedicated readme file explaining how to install this tool and run these tests from command line.  

## Demo

Demo is hosted on Google Cloud Container claster: **104.155.121.39** 


```
export TOKEN=$(curl -X POST \
  http://104.155.121.39:8080/oauth/token \
  -H 'authorization: Basic dmlkZW9jbGllbnQ6' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d 'username=videouser&password=p%40ssw0rd&grant_type=password' | jq --raw-output '.access_token')
```

```
wget http://www.sample-videos.com/video/mp4/480/big_buck_bunny_480p_1mb.mp4 -O sample.mp4
```

```
curl -X POST \
  http://104.155.121.39:8080/video/stat \
  -H 'accept: application/json' \
  -H "authorization: Bearer $TOKEN" \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data' \
  -F 'video=@sample.mp4;type=video/mp4'
```