package com.ibasco.pidisplay.core;

import java.util.Optional;

public interface Dialog<A> {
    Optional<A> getResult();
}
