package reactiongame.support;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Streams {

    private Streams() {
    }

    public static <T, R> Stream<R> mapWithIndex(
            final Stream<T> stream,
            FunctionWithIndex<? super T, ? extends R> function
    ) {
        final Spliterator<R> spliterator = createSpliterator(function, stream.spliterator());
        return StreamSupport.stream(spliterator, stream.isParallel())
                .onClose(stream::close);
    }

    private static <T, R> Spliterator<R> createSpliterator(
            final FunctionWithIndex<? super T, ? extends R> function,
            final Spliterator<T> streamSpliterator
    ) {
        if (!streamSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
            return new NotSubsizedSpliterator<>(function, streamSpliterator);
        }

        return new SubsizedSpliterator<>(function, streamSpliterator);
    }

    public interface FunctionWithIndex<T, R> {

        R apply(long index, T from);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final class NotSubsizedSpliterator<T, R>
            extends Spliterators.AbstractSpliterator<R> {

        private final FunctionWithIndex<? super T, ? extends R> function;

        private final Iterator<T> fromIterator;

        private long index;

        public NotSubsizedSpliterator(
                final FunctionWithIndex<? super T, ? extends R> function,
                final Spliterator<T> spliterator
        ) {
            super(
                    spliterator.estimateSize(),
                    spliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)
            );

            this.function = function;
            this.fromIterator = Spliterators.iterator(spliterator);
        }

        @Override
        public boolean tryAdvance(final Consumer<? super R> action) {
            if (fromIterator.hasNext()) {
                action.accept(function.apply(incrementIndexAndGet(), fromIterator.next()));
                return true;
            }
            return false;

        }

        private long incrementIndexAndGet() {
            return index++;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final class SubsizedSpliterator<T, R>
            extends MapWithIndexSpliterator<Spliterator<T>, R, SubsizedSpliterator<T, R>>
            implements Consumer<T> {

        T holder;
        private final FunctionWithIndex<? super T, ? extends R> function;

        private final Spliterator<T> spliterator;

        SubsizedSpliterator(
                final FunctionWithIndex<? super T, ? extends R> function,
                final Spliterator<T> spliterator
        ) {
            this(function, spliterator, 0);
        }

        SubsizedSpliterator(
                final FunctionWithIndex<? super T, ? extends R> function,
                final Spliterator<T> spliterator,
                final long index
        ) {
            super(spliterator, index);
            this.function = function;
            this.spliterator = spliterator;
        }

        @Override
        public void accept(final T t) {
            this.holder = t;
        }

        @Override
        public boolean tryAdvance(final Consumer<? super R> action) {
            if (!spliterator.tryAdvance(this)) {
                return false;
            }

            try {
                action.accept(function.apply(incrementIndexAndGet(), holder));
                return true;
            } finally {
                holder = null;
            }
        }

        @Override
        SubsizedSpliterator<T, R> createSplit(
                final Spliterator<T> from,
                final long index
        ) {
            return new SubsizedSpliterator<>(
                    function,
                    from,
                    index
            );
        }
    }

    private abstract static class
    MapWithIndexSpliterator<F extends Spliterator<?>, R, S extends MapWithIndexSpliterator<F, R, S>>
            implements Spliterator<R> {

        private final F fromSpliterator;

        private long index;

        MapWithIndexSpliterator(
                final F fromSpliterator,
                final long index
        ) {
            this.fromSpliterator = fromSpliterator;
            this.index = index;
        }

        abstract S createSplit(F from, long index);

        @Override
        public final Spliterator<R> trySplit() {
            final var splitOrNull = fromSpliterator.trySplit();
            if (splitOrNull == null) {
                return null;
            }

            @SuppressWarnings("unchecked") final var split = (F) splitOrNull;
            final var result = createSplit(split, index);
            this.index += split.getExactSizeIfKnown();
            return result;
        }

        @Override
        public final long estimateSize() {
            return fromSpliterator.estimateSize();
        }

        @Override
        public final int characteristics() {
            return fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
        }

        protected long incrementIndexAndGet() {
            return index++;
        }
    }
}
