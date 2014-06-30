#!/bin/bash

#curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"filename":"here","sum":"123"}' http://127.0.0.1:8080/files 
#curl  -H "Accept: application/json" -H "Content-Type: application/json"  -d '{"id":1,"name":"123"}' http://127.0.0.1:8080/files

curl -i -H "Accept: application/json" -H "Content-Type: application/json"  -d '{"id":1,"name":"123"}' --data-binary "@image.png"   http://127.0.0.1:8080/uploadjs
