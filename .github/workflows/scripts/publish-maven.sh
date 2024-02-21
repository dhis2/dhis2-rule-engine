set -x

branch=$(git rev-parse --abbrev-ref HEAD)

if [ "$branch" = "main" ] || [ "$branch" = "master" ]; then
  ./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
elif [ "$branch" = "beta" ]; then
  ./gradlew publishToSonatype -PbetaToSnapshot
fi
