package de.jowisoftware.rpgsoundscape.player.threading;

public sealed interface StackResult {
    static StackResult finish() {
        return new StackResult.Finish();
    }

    static StackResult enter(ThreadStep next) {
        return new StackResult.Enter(next);
    }

    static StackResult replace(ThreadStep next) {
        return new StackResult.Replace(next);
    }

    final class Finish implements StackResult {
        private Finish() {};
    };

    final class Replace implements StackResult {
        public final ThreadStep next;

        private Replace(ThreadStep next) {
            this.next = next;
        }
    };

    final class Enter implements StackResult {
        public final ThreadStep next;

        private Enter(ThreadStep next) {
            this.next = next;
        }
    };
}
