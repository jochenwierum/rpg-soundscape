package de.jowisoftware.rpgsoundscape.player.threading;

import de.jowisoftware.rpgsoundscape.player.threading.StackResult.Enter;
import de.jowisoftware.rpgsoundscape.player.threading.StackResult.Finish;
import de.jowisoftware.rpgsoundscape.player.threading.StackResult.Replace;

import java.util.Stack;

public class CallStack {
    private final Stack<ThreadStep> stack = new Stack<>();

    public void reset(ThreadStep threadStep) {
        stack.clear();
        stack.push(threadStep);
    }

    public boolean next(TrackExecutionContext context) {
        StackResult next = stack.peek().apply(context);
        if (next instanceof Finish) {
            stack.pop();
        } else if (next instanceof Enter enter) {
            stack.push(enter.next);
        } else if (next instanceof Replace replace) {
            stack.pop();
            stack.push(replace.next);
        }

        return !stack.isEmpty();
    }
}
