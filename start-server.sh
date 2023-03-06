#!/bin/sh
cd /data/  

if "$DEBUG" = true; then
	java -jar Service.jar update
else
	java -jar  Service.jar
fi