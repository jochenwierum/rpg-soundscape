#!/bin/bash

./gradlew clean :distribution:assemble
test -f soundscape.zip && rm soundscape.zip
mv distribution/build/distributions/soundscape.zip .

test -f soundscape-src.zip && rm soundscape-src.zip
zip soundscape-src.zip $(find . \( -name node_modules -o -name out -o -name build -o -name .git -o -name .gradle -o -name .cache -o -name idea-sandbox -o -name .idea -o -name soundscape.zip \) -prune -o -print)
