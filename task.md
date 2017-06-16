Task: Video Metadata API

1. Application should expose a REST API endpoint for inspecting a single video file. It should accept only video files and return the Video metadata information (JSON) including:
- duration
- video size
- video bit rate
- video codec
- audio bit rate
- audio codec

2. The technology stack should include:
- Java 8
- Spring boot
- ffmpeg (or any wrapper)
- Maven
- Docker

3. Application should include a readme file that includes information how to build and run application

4. The solution should be provided as a link to public git repo and a link to a demo version hosted on any available server.

5. During implementation, please take into account the following aspects:
- REST API design best practices
- validation and error handling (API should accept ONLY videos with MAX size = 25 MB)
- tests (unit tests and api-tests)
- api authentication (it's up to you what authentication mechanism to use)
