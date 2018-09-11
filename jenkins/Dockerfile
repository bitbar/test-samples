FROM jenkins/jenkins:lts
RUN /usr/local/bin/install-plugins.sh testdroid-run-in-cloud
USER root
RUN apt-get update && apt-get install -y python-dev python-pip
RUN pip install testdroid
EXPOSE 8080
USER jenkins