set -x

OUTPUT_DIR="packageJs"
sh $(dirname "$0")/build-npm-package.sh $OUTPUT_DIR -PremoveSnapshotSuffix

cd $OUTPUT_DIR || exit

# Set authentication token for npmjs registry
npm set //registry.npmjs.org/:_authToken="$NPMJS_TOKEN"

npm publish
