package com.dimitar.fe404sleepnotfound.persistence;

import com.dimitar.fe404sleepnotfound.data.Hint;

public interface HintDAO {
    /**
     * Used to use results from async tasks
     * @param <T>
     */
    @FunctionalInterface
    interface Consumer<T> {
        void consume(T data);
    }

    /**
     * Reads all the hints from a data source
     *
     * @param consumer uses the result asynchronously
     */
    void readAllHints(Consumer<Hint[]> consumer);
}
