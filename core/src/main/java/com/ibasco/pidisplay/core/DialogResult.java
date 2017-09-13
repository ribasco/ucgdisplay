package com.ibasco.pidisplay.core;

public class DialogResult<A> {
    private A result;

    public DialogResult(A result) {
        this.result = result;
    }

    public A getResult() {
        return result;
    }
}
