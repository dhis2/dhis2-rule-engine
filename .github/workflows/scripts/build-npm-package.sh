set -x

OUTPUT_DIR=$1
GRADLE_PARAMS=$2

BUILD_DIR="build/packages/js"

rm -rf $OUTPUT_DIR && mkdir $OUTPUT_DIR
./gradlew clean

./gradlew packJsPackage $GRADLE_PARAMS
mv $BUILD_DIR/* $OUTPUT_DIR/

./gradlew packJsPackage -PuseCommonJs $GRADLE_PARAMS
rm $BUILD_DIR/package.json
mv $BUILD_DIR/* $OUTPUT_DIR/
