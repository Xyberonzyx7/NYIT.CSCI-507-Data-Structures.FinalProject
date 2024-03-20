#!/bin/bash

# remove all .class in sub directories
find . -type f -name "*.class" -exec rm -f {} \;
