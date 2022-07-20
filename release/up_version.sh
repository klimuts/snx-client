#!/bin/bash
### Increment the selected part and put zeros on the subsequent parts
## major 1.39.3 -> 2.0.0
## minor 1.39.3 -> 1.40.0
## build 1.39.3 -> 1.39.4

# $1: version
# $2: number of part: 0 – major, 1 – minor, 2 – build
increment_version() {
  local delimiter=.
  local array=($(echo "$1" | tr $delimiter '\n'))
  array[$2]=$((array[$2] + 1))
  if [ $2 -lt 2 ]; then array[2]=0; fi
  if [ $2 -lt 1 ]; then array[1]=0; fi
  echo $(local IFS=$delimiter; echo "${array[*]}") | tee ../src/main/resources/version
}

version=$(cat ../src/main/resources/version)

# Check parameter to see which number to increment
if [ "$1" == "--major" ]; then
  increment_version "$version" 0
elif [ "$1" == "--minor" ]; then
  increment_version "$version" 1
elif [ "$1" == "--build" ]; then
  increment_version "$version" 2
else
  echo "Usage: bash up_version.sh --[major/minor/build]"
  exit 1
fi

new_version=$(cat ../src/main/resources/version)

# Create an appropriate git tag
#git tag v"$new_version"

# Change the version in the Readme files
sed -i "1c\# SNX Client v$new_version" ../doc/README.md
sed -i "1c\# SNX Client v$new_version" ../README.md
