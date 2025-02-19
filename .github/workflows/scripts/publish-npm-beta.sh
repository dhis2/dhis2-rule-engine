set -x

OUTPUT_DIR="packageJs"
sh $(dirname "$0")/build-npm-package.sh $OUTPUT_DIR

cd $OUTPUT_DIR || exit

# Set authentication token for npmjs registry
npm set //registry.npmjs.org/:_authToken="$NPMJS_TOKEN"

# Set 'beta' suffix in the version starting with beta.0
sed -i -e 's/-SNAPSHOT"/-beta.0"/g' package.json

# Try to upload the beta version. If it is not available, increase the beta number and try again.
# Iterate a maximum of MAX_ITERATION times.
n=1
MAX_ITERATIONS=30
while [ $n -le $MAX_ITERATIONS ]; do
  if ! output=$(npm publish --tag beta 2>&1); then
    if [[ "$output" == *"ERR! code E403"* ]]; then
      # This error code is likely to be thrown when the version already exists
      # Increase beta version number to try again
      npm version prerelease
      n=$(( n + 1 ))
    else
      exit 1
    fi
  else
    # If upload is successful, exit
    exit 0
  fi
done
