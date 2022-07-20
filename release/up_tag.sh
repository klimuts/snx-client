#!/bin/bash
# Create a git tag according to the version of the application

version=$(cat ../src/main/resources/version)

git tag v"$version"