package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.wrapper;

import jdk.incubator.foreign.Addressable;
import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.CLinker.TypeKind;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemoryLayout.PathElement;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.SequenceLayout;
import jdk.incubator.foreign.ValueLayout;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

abstract class AbstractMemoryWrapper implements Addressable {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private final MemoryLayout memoryLayout;
    private final String name;

    protected MemorySegment segment;

    public AbstractMemoryWrapper(String name, MemoryLayout memoryLayout, MemorySegment segment) {
        this.segment = segment;
        this.memoryLayout = memoryLayout;
        this.name = name;
    }

    public AbstractMemoryWrapper(String name, MemoryLayout memoryLayout) {
        this(name, memoryLayout, (MemorySegment) null);
    }

    public AbstractMemoryWrapper(String name, MemoryLayout memoryLayout, MemoryAddress memoryAddress) {
        this(name, memoryLayout, memoryAddress.asSegmentRestricted(memoryLayout.byteSize()));
    }

    @Override
    public MemoryAddress address() {
        return segment.address();
    }

    protected void withSegmentPointer(Consumer<MemorySegment> consumer) {
        try (MemorySegment pointer = MemorySegment.allocateNative(CLinker.C_POINTER.byteSize())) {
            MemoryAccess.setAddress(pointer, segment);
            consumer.accept(pointer);
        }
    }

    public String toString() {
        if (!(memoryLayout instanceof GroupLayout groupLayout)) {
            return "%s [%s] is not a group layout".formatted(name, memoryLayout.name().orElse("(unknown)"));
        }

        StringBuilder value = new StringBuilder("Dump of ")
                .append(name)
                .append(" 0x")
                .append("%X".formatted(segment.address().toRawLongValue()))
                .append(" [")
                .append(memoryLayout.name().orElse("(unknown)"))
                .append("]\n");

        for (MemoryLayout memberLayout : groupLayout.memberLayouts()) {
            memberLayout.name().ifPresentOrElse(memberName -> {
                        long size = memberLayout.byteSize();
                        long offset = memoryLayout.byteOffset(PathElement.groupElement(memberName));
                        MemorySegment slice = segment.asSlice(offset, size);

                        value.append("  * ")
                                .append(memberName)
                                .append(" @ 0x")
                                .append("%X".formatted(slice.address().toRawLongValue()))
                                .append(" (")
                                .append(offset)
                                .append("+")
                                .append(size)
                                .append(") ");

                        if (memberLayout instanceof SequenceLayout sl
                                && sl.hasSize()
                                && sl.elementLayout() instanceof ValueLayout slvl
                                && slvl.attribute("abi/kind").orElse(null) instanceof TypeKind slkd) {

                            if (slkd == TypeKind.CHAR) {
                                value.append("\"");
                                ByteBuffer bb = slice.asByteBuffer().order(slvl.order());
                                byte b = bb.get();
                                while (b != 0 && bb.hasRemaining()) {
                                    value.append((char) b);
                                    b = bb.get();
                                }
                                value.append("\" [CHAR*]");
                            } else {
                                ByteBuffer bb = slice.asByteBuffer().order(slvl.order());
                                long count = sl.elementCount().orElseThrow();
                                for (long i = 0; i < count; i++) {
                                    if (i > 0) {
                                        value.append(", ");
                                    }
                                    appendValue(value, slkd, bb);
                                }
                                value.append(" [")
                                        .append(slkd.name())
                                        .append("[")
                                        .append(count)
                                        .append("]]");
                            }
                        } else if (memberLayout instanceof ValueLayout vl
                                && vl.attribute("abi/kind").orElse(null) instanceof TypeKind kd) {
                            ByteBuffer bb = slice.asByteBuffer().order(vl.order());
                            appendValue(value, kd, bb);
                            value.append(" [").append(kd.name()).append("]");
                        } else {
                            describeUnknown(slice, value);
                        }
                        value.append("\n");
                    },
                    () -> value.append("  * (unnamed: ").append(memberLayout.byteSize()).append(" bytes)\n"));
        }

        return value.toString();
    }

    private void appendValue(StringBuilder value, TypeKind kd, ByteBuffer bb) {
        value.append(switch (kd) {
            case CHAR -> String.format("0x%X", bb.get());
            case SHORT -> String.format("0x%X", bb.asShortBuffer().get());
            case INT -> String.format("0x%X", bb.asIntBuffer().get());
            case LONG -> String.format("0x%X", bb.asLongBuffer().get());
            case FLOAT -> String.format("%.2f", bb.asFloatBuffer().get());
            case DOUBLE -> String.format("%.2f", bb.asDoubleBuffer().get());
            case POINTER -> {
                long address = bb.asLongBuffer().get();
                yield address == 0 ? "NULL" : String.format("0x%X", address);
            }
            default -> "?";
        });
    }


    private static void describeUnknown(MemorySegment slice, StringBuilder value) {
        value.append("0x");
        for (byte aByte : slice.toByteArray()) {
            int v = aByte & 0xFF;
            value.append(HEX_ARRAY[v >>> 4]);
            value.append(HEX_ARRAY[v & 0x0F]);
        }
        value.append(" [?]");
    }
}
