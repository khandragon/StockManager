package com.dimitar.fe404sleepnotfound.data;


public class CircularArray<T> {
    private final T[] backingArray;
    private int currIndex;

    private OnChangeCurrentEventListener<T> changeCurrentListener;
    private void fireChangeCurrentEvent() {
        if(changeCurrentListener != null) {
            changeCurrentListener.consumeCurrent(backingArray[currIndex]);
        }
    }
    public interface OnChangeCurrentEventListener<T> {
        void consumeCurrent(T newCurrent);
    }

    public CircularArray(T... backingArray) {
        Object[] newBackingArray = new Object[backingArray.length];
        System.arraycopy(backingArray, 0, newBackingArray, 0, backingArray.length);

        this.backingArray = (T[]) newBackingArray;
        currIndex = 0;
    }

    public void next() {
        currIndex = (currIndex + 1) % backingArray.length;

        fireChangeCurrentEvent();
    }

    public void prev() {
        currIndex--;
        while(currIndex < 0) {
            currIndex = backingArray.length - currIndex;
        }

        fireChangeCurrentEvent();
    }

    public void onCurrentChange(OnChangeCurrentEventListener<T> changeCurrentListener) {
        this.changeCurrentListener = changeCurrentListener;
    }

    public T getCurrent() {
        return backingArray[currIndex];
    }
}