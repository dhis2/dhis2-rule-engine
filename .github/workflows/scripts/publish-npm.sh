set -x

branch=$(git rev-parse --abbrev-ref HEAD)

./gradlew packJsPackage
./gradlew packJsPackage -PuseCommonJs

cd build/packages/js || exit

# Set authentication token for npmjs registry
npm set //registry.npmjs.org/:_authToken="$NPMJS_TOKEN"

if [ "$branch" = "main" ] || [ "$branch" = "master" ]; then
  npm publish
elif [ "$branch" = "beta" ]; then
  npm publish --tag beta
fi
