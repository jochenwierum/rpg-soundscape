# How To

[TOC]

## First steps

1. Launch either `bin/player` or `bin/player.bat`
2. Write `.soundscape` files in the `library/` folder
3. Navigate your browser to http://127.0.0.1:8008/

## Building a Soundscape file

A soundscape consists of a few building blocks. Only the name and the tracks are mandatory:

* A Name
* Tracks
* Sample definitions (optional)
* Includable tracks (optional)
* Metadata (optional description and category)

A soundscape is defined on the root level of a `.soundscape` file:

~~~
// inside a file named "nature.soundscape":

// ← this is a comment
/* ← this is also a (multiline) comment → */

Soundscape "forest" {
  // define tracks and other things here…
} 

// all keywords are case insensitive
SOUNDSCAPE "sea" {
  // …
}
~~~

These files must be placed inside the `library` folder.
Changes are detected automatically, and the files are automatically reloaded.

## Samples

Every playable sound must be defined as a "sample".
A sample is "resolved" when the soundscape is loaded.
It can be used multiple times and in multiple soundscapes.

A sample has a unique name and a URI.
The supported URIs and file types are described later in this document (see: Sources).

A sample is defined in the root level of a `.soundscape` file or inside a `soundscape`:

~~~
Load sample rain from "file:////rain.mp3";

Soundscape "storm" {
  Load sample thunder from "file:////thunder.mp3";
  // "thunder" and "rain" can be used here...
} 

Soundscape "forest" {
  // here, only rain is available
}

Soundscape "sea" {
  Load sample thunder from "file:////far-thunder.mp3";
  // "thunder" and "rain" can be used here...
}
~~~

### Modifying samples

A sample can be modified globally. At the moment there are three modifications:

* amplification: increase or reduce the volume of the sample
* omission: skip a part of the sample at the beginning
* limit: skip a part of the sample at the end

Modifications are introduced with a `with` keyword.
Multiple modifications are separated with comma or `and`.

~~~
Load sample rain from "file:////rain.mp3"
  with omission of first 2s;

Load sample birds from "file:////birds.mp3"
  with limit to 1m;

Load sample horses from "file:////horses.mp3"
  with amplification of -5%

Load sample campfire from "file:////campfire.mp3"
  with amplification of -5%,
  with limit to 1m and
  with omission of first 500ms;
~~~

For skipping, the units 'ms', 's' and 'm' are valid. There is no space in front of the unit (i.e. `10 s` is invalid!) 

### Controlling caching

Normally, all files are converted into a format that can be played really quickly.
However, this format produces huge files.
For large mp3 files (such as complete soundscapes or long music tracks) this caching can be disabled.
Opening such a sample takes significantly more time!

~~~
Load sample campfire from "file:////ambience-town.mp3"
  without conversion cache;
~~~

### Attributions

Some samples require attributions. These are texts that normally mention the author.
Some Sources (e.g. `soundscape://`) automatically add attributions.
Attributions work like modifications:

~~~
Load sample campfire from "file:////campfire.mp3"
  with attributions "Recorded by John Doe";
~~~

The attributions are shown in the description of the soundscape.


## Sources

The following files formats are supported:

 * wav
 * au
 * mp3
 * ogg

### Using files

The simplest type of source is a file. Files must be saved in the library folder.
They are references with `file://` followed by the path inside the library folder.
The path itself must start with `/` (which makes three slashes in total!).  

~~~
// load rain.mp3 from the library folder directly:
Load sample rain from "file:///rain.mp3";

// load rain.mp3 from the library sounds folder:
Load sample thunder from "file:///sounds/thunder.mp3";
~~~

### Using HTTP(s)-URLs

Another way to import a sound is a http or https URL:

~~~
Load sample dragon from "http://192.168.0.1/sounds/dragon.mp3";
Load sample dragon from "https://mysounds.example/dragon.mp3";
~~~

### Using Sounds from Freesound

[Freesound](https://freesound.org/) is a huge database of free [samples](https://freesound.org/browse/).
You can browse and listen to these samples on the websites.
If you want to use these samples in a soundscape, a free account is required.

Every sound has a unique id, which can be found in the url.
This id must be used to reference a sound:

~~~
Load sample muddy_steps from "freesound:///329604";
// https://freesound.org/people/InspectorJ/sounds/329604/
~~~

Attributions for freesound samples are automatically downloaded.

If a freesound URL is found, the download is _not_ started because a login is required.
Visit [the webinterface](https://127.0.0.1:8008) and follow the link in the problem tab.
Once a file is downloaded, it is cached for further uses. 

### Using Sounds from Youtube and co.

If [youtube-dl](http://youtube-dl.org/) is installed on your system, you can also reference samples that will be downloaded with youtube-dl.
Please make sure you are allowed to do this!

Just use the normal video url and prepend `youtubedl+`:

~~~
Load sample some_music from "youtubedl+https://my-video-site.com/1234";
~~~

## Soundscapes and Tracks

Every soundscape needs at least one track.
Tracks define which samples appear in which order and how often.
A track can be paused and resumed. It should be a logical unit such as
"background noise", "footsteps" or "birds 1".

There are two types of tracks: looping tracks and manual tracks.

A looping track is repeated as soon as it finished playing.
Examples for a looping tracks could be rain, wind, talking people or other ambient sounds.
A looping track can also start paused and must be activated by the DM.

A manual track is played just once and must be started by the DM on demand.
Examples for a manual track could be a scream or a slamming door.
A manual track can also be automatically played once the soundscape starts. 

Tracks are defined inside of a soundscape. Every Track must have a name:

~~~
Soundscape "forest" {
  // (starts when the soundscape is played and repeats, can be paused anytime)
  Looping track wind {
    // track statements …
  }

  // (must be started by the DM and repeats, can be paused anytime)
  Looping paused track animals {
    // track statements …
  }

  // (must be started by the DM and does not repeat)
  Manual track scream {
    // track statements …
  }

  // (is played once on start, must be otherwise started by the DM and does not repeat)
  Manual autostarting track falling_rocks {
    // track statements …
  }
}
~~~

Tracks may also have a title. That title is only used on the website and is fully optional:

~~~
Soundscape "forest" {
  Looping track birds with title "singing birds" {
    // track statements …
  }
}
~~~

### Building Tracks 

Tracks themselves consists of a few building blocks.

#### Playing a sample

The most useful statement is `play`. It plays a sample:

~~~
Load sample wind1 from "file:///wind1.mp3"; 
Load sample wind2 from "file:///wind2.mp3"; 

Soundscape "forest" {
  Looping track wind {
    Play wind1;
    Play wind2;
  }
}
~~~

Sometimes it's useful to modify a sample before it is played.

~~~
Load sample wind1 from "file:///wind1.mp3"; 
Load sample wind2 from "file:///wind2.mp3"
    with amplification of 10%; 

Soundscape "forest" {
  Looping track wind {
    // amplified by 15%:
    Play wind1 with amplification of 15%;
    
    // amplified by 25%:
    Play wind2 with amplification of 15%;
  }
}
~~~

See "modifying samples" above for all supported modifications.

### Pausing

The second most important statement is `sleep`.

~~~
Soundscape "tavern" {
  Looping track people {
    Play laughing;
    Sleep 500ms; // always sleep 500 ms
    Play laughing;
    Sleep between 500ms and 5s; // sleep a random time
  }
}
~~~

Milliseconds (`ms`), seconds (`s`) and minutes (`m`) are supported.

### Adding more randomness

For a dynamic scene, randomness is important. The `randomly` statement helps here:

~~~
Soundscape "tavern" {
  Looping track people {
    Randomly {
        Weighted with 3 do nothing;
        Weighted with 2 play bird1;
        Weighted with 2 play bird2;
        Weighted with 1 {
          Play bird3;
          Play bird4;
        }
        Weighted with 1 {
            Play bird4;
            Play bird2;
       }
    }
  }
}
~~~

This means: with a chance of 3/9 (= 1/3) do nothing. With the chance of 2/9 play the sample bird1, and so on.

### Repeating things

Loops help to repeat _parts of a track_ multiple times.
Loops can be static (`Repeat X times`) or randomized (`Repeat between X and Y times`).
If a range is used, both counts are inclusive.

~~~
Soundscape "tavern" {
  Looping track money {
    Repeat 2 times {
      Play coins;
      Sleep 100ms;
    }

    Repeat between 2 and 8 times {
      Play coins;
    }
  }
}
~~~

### Doing Multiple Things in Parallel

In rare cases it makes sense to do multiple things in parallel in a single track. This is possible with `parallely`:

~~~
Soundscape "tavern" {
  Looping track money {
    Parallelly {
      play coins;
      { 
        sleep between 50 and 150ms;
        play coins;
      }
      {
        sleep 200ms;
        play coins;
      }
    }
  }
}
~~~

### Pausing and Resuming other Tracks

In advanced soundscapes, it might be useful to modify other tracks:

~~~

Soundscape "battle" {
  Manual track crescendo {
    Play dragon;
    Pause all other tracks;
    Play screams;
  }
  
  // …
}
~~~

The following modifications are possible:

 * `Pause all other tracks`
 * `Pause track X` (`X` is the id of a track in this soundscape)
 * `Pause all tracks`
 * `Pause this track`
 * `Resume track X` (`X` is the id of a track in this soundscape)
 * `Resume looping tracks` (resumes all looping tracks)

## Resusing Tracks and Samples

Some parts of soundscapes are so important that they are used in different places. There are two three to share them:

### Including other files

Any file can include any other file:

~~~
// in file1.soundscape:
include "_common_city_sounds.soundscape"
~~~

The included file name should start with an underscore (`_`).
Such files are excluded and not automatically imported in the library.

It is recommended to define only samples ("`Load sample`"), includable soundscapes and includable tracks inside those files.
I.e., adding soundscapes, effects or music would lead to multiple imports if the file is included several times.  

### Reusing Tracks

Sometimes there are whole tracks that are worth to be reused.
Such tracks can be defined inside or outside of a soundscape.
An importable track can be referenced inside a normal track - possibly mixed with other statements:

~~~
Load sample bird1 from "file:///birds.wav";
// could also be included from a common file:
includable track _track_birds { 
  play bird1;
}

Soundscape "forest" {
  looping track birds {
    // possible more statements here
    include _track_birds;
    // possible more statements here
  }

  looping track other_track {
    // …
  }
}

Soundscape "sea" {
  manual autostarting track birds {
    include _track_birds;
  }
}
~~~

It is also allowed to define them inside a soundscape but there are only a few cases where this makes sense
(e.g. to define two identical tracks). 

### Reusing soundscapes

It is also possible to define parts of a soundscape which can be reused in multiple soundscapes:

~~~
Load sample bird1 from "file:///birds.wav";
// could also be included from a common file:
includable soundscape _birds {
  looping track birds { 
    play bird1;
  }
}

Soundscape "forest" {
  include _birds;
  include _common_metadata;

  looping track birds {
    // possible more statements here
    include _track_birds;
    // possible more statements here
  }
}
~~~

If multiple soundscapes are included with conflicting tracks, the last include wins.

## Music and Effects

Some samples (e.g. spells) are so special that the DM would only activate them manually in different scenes.
These samples can be defined as "effects" outside of any soundscape:

```
Load sample spell from "file:///spell.mp3";

Effect "mighty spell" from spell;
```

The same is valid for background music:

```
Load sample music_calm from "file:///music-calm.mp3";

Music "calm music" from music_calm; 
```

## Descriptions and Categories

It is possible to describe soundscapes, effects and music with descriptions and categories.
Descriptions are shown in the info page of the item.
The categories are used to filter the items in the search page. 

```
Soundscape "town" {
  described as "a medival town";
  categorized in "mood" as "dark";
  categorized in "environment" as "city";
  // tracks…
}

Music "happy" from happy {
  described as "a nice and sunny music";
  // it's both: sunny and feelgood:
  categorized in "mood" as "sunny";
  categorized in "mood" as "feelgood";
  categorized in "environment" as "city";
}


Effect "Spell" from spell3 {
  described as "some spell";
  categorized in "environment" as "fight";
}
```
