# rpg-soundscape

This is a personal learning project.

My goal was to build a [soundscape](https://en.wikipedia.org/wiki/Soundscape) generator for role playing games. I wanted to build in IntelliJ plugin to edit the soundscapes and a standalone player which would work on an Raspberry Pi. I also experimented with new (preview) java features.

Feel free to use or change the project. However, the project is not production ready from my point of view.

## Build and Run

Checkout the project, then run

```
./gradlew assemble
```

You'll find the software, including the IntelliJ plugin in rpg-soundscape/distribution/build/distributions. Simply unzip the plugin and run `bin/player`.

You can modify the file `config.properties` to your own needs.

## About IntelliJ's Parser

This software uses IntelliJ's Grammar-Kit project for the plugin. It also uses it's light-psi proof of concept to parse the soundscape files outside of IntelliJ.

Grammar-Kit was modified a tiny bit to generate those light-psi library. These changes are available on [GitHub](https://github.com/jochenwierum/Grammar-Kit), too.
