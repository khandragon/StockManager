package com.dimitar.fe404sleepnotfound.persistence;

import com.dimitar.fe404sleepnotfound.data.Hint;

public interface HintDAO {
    interface Consumer<T> {
        void consume(T data);
    }

    void readAllHints(Consumer<Hint[]> consumer);
}
