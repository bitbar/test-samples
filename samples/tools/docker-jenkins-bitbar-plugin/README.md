# Jenkins with Bitbar Plugin

This is a simple Dockerfile for starting a Jenkins instance with the Bitbar cloud plugin and API client installed.

1. `docker build --tag bitbar-jenkins .`
1. `docker run -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts`
1. Go to <http://localhost:8080/> and complete the wizard

    1. Install suggested plugins
    1. Create admin user
    1. Create your build
