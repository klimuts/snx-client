cd ..
rm -rf build &&
./gradlew jlink &&
./gradlew copyBin &&
./gradlew zipDistrImage