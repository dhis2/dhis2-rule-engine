# Check if new version
./gradlew checkIsNewVersion -q; newVersion=$?

if [ $newVersion -ne 0 ]; then
  echo "isNewVersion=false" >> "$GITHUB_OUTPUT"
else
  echo "isNewVersion=true" >> "$GITHUB_OUTPUT"
fi