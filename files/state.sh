#!/bin/bash

passcode=$1

if [[ -n "$passcode" ]]; then
    echo "WARNING! DO NOT CLOSE THIS WINDOW! CONNECTION IN PROGRESS..."
    echo "$passcode" | snx |& tee state
else
    echo "Passcode error"
fi
