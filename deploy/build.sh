cd ..
rm -rf build &&
./gradlew clean &&
./gradlew jlink &&
./gradlew copyBin &&
./gradlew zipDistrImage