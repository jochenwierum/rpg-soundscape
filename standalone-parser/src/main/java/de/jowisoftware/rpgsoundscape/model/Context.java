package de.jowisoftware.rpgsoundscape.model;

import de.jowisoftware.rpgsoundscape.language.psi.SoundscapeFile;

import java.util.Map;
import java.util.Set;

interface Context {

    class RootContext implements Context {

        @Override
        public Sample sample(String name) {
            return null;
        }

        @Override
        public Block includableTrack(String name) {
            return null;
        }

        @Override
        public boolean knowsTrack(String name) {
            return false;
        }

        @Override
        public Soundscape includableSoundscape(String name) {
            return null;
        }
    }

    abstract class DerivedContext implements Context {
        private final Context parent;

        public DerivedContext(Context parent) {
            this.parent = parent;
        }

        @Override
        public Sample sample(String name) {
            return parent.sample(name);
        }

        @Override
        public Statement includableTrack(String name) {
            return parent.includableTrack(name);
        }

        @Override
        public boolean knowsTrack(String name) {
            return parent.knowsTrack(name);
        }

        @Override
        public Soundscape includableSoundscape(String name) {
            return parent.includableSoundscape(name);
        }
    }

    Sample sample(String name);

    Statement includableTrack(String name);

    Soundscape includableSoundscape(String name);

    boolean knowsTrack(String name);

    static Context create() {
        return new RootContext();
    }

    default Context withAdditionalSamples(Map<String, Sample> samples) {
        return new DerivedContext(this) {
            @Override
            public Sample sample(String name) {
                Sample result = samples.get(name);
                return result != null ? result : super.sample(name);
            }
        };
    }

    default Context withAdditionalIncludableTracks(Map<String, Statement> includableTracks) {
        return new DerivedContext(this) {
            @Override
            public Statement includableTrack(String name) {
                Statement result = includableTracks.get(name);
                return result != null ? result : super.includableTrack(name);
            }
        };
    }

    default Context withTrackNames(Set<String> trackNames) {
        return new DerivedContext(this) {
            @Override
            public boolean knowsTrack(String name) {
                return trackNames.contains(name) || super.knowsTrack(name);
            }
        };
    }

    default Context withFile(SoundscapeFile f) {
        return new DerivedContext(this) {

        };
    }

    default Context withIncludableSoundscapes(Map<String, Soundscape> includableSoundscapes) {
        return new DerivedContext(this) {
            @Override
            public Soundscape includableSoundscape(String name) {
                return includableSoundscapes.get(name);
            }
        };
    }
}
