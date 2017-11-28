#!/bin/bash

find -iname "*.pom" -exec mvn -f {} install \;
