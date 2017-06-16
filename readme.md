# Videostat

Videostat is a simplistic application which investigate video file and returns some video metadata. 

Application name refers to similarity with unix *stat* function.

[![CircleCI](https://circleci.com/gh/machomic/videostat.svg?style=svg)](https://circleci.com/gh/machomic/videostat)

## Caveats

Application requires FFmpeg library to be installed on the host system. To install it go to https://ffmpeg.org/.

Upload file size is limited to 25MB.

## Configuration 

TBD

## Development

Application is prepared using maven and distributed as spring boot jar (tomcat embedded). Most IDEs will recognize and build it automatically. 

Maven wrapper is also included so you would not have install maven on your host.

To simply run it from command line just type `./mvnw`

## Exposed endpoints

There is only one pretty useful POST endpoint: http://localhost:8080/video/meta

Example curl command to use it:

`curl -X POST   http://localhost:8080/video/stat  -H "content-type: multipart/form-data" -F "video=@sample.mp4;type=video/mp4"`

## Testing

TBD

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

## Api Testing

TBD

## Demo

TBD