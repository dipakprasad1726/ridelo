#!/bin/bash
set -xe


# Copy war file from S3 bucket to tomcat webapp folder
aws s3 cp s3://arn:aws:s3:::codedeploystack-webappdeploymentbucket-12fnmzouqydsu/ridelo.war /usr/local/tomcat9/webapps/ridelo.war


# Ensure the ownership permissions are correct.
chown -R tomcat:tomcat /usr/local/tomcat9/webapps