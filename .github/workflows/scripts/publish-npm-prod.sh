set -x

./gradlew packJsPackage
./gradlew packJsPackage -PuseCommonJs

cd build/packages/js || exit

# Set authentication token for npmjs registry
npm set //registry.npmjs.org/:_authToken="$NPMJS_TOKEN"

npm publish
