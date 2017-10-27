package com.ibasco.pidisplay.core.beans;

import java.util.List;

@FunctionalInterface
public interface ListChangeListener<T> {
    abstract class Change<T> {
        private final List<T> list;

        public Change(List<T> list) {
            this.list = list;
        }

        abstract public List<T> getRemoved();

        abstract public int getFromIndex();

        abstract public int getToIndex();

        public boolean added() {
            return /*!wasPermutated() && !wasUpdated()*/getFromIndex() < getToIndex();
        }

        public boolean replaced() {
            return added() && removed();
        }

        public boolean removed() {
            if (getRemoved() == null)
                return false;
            return !getRemoved().isEmpty();
        }

        public List<T> getList() {
            return list;
        }
    }

    void onChange(Change<? extends T> change);

    final class AddChange<T> extends Change<T> {

        public AddChange(List<T> list) {
            super(list);
        }

        @Override
        public boolean added() {
            return true;
        }

        @Override
        public List<T> getRemoved() {
            return null;
        }

        @Override
        public int getFromIndex() {
            return 0;
        }

        @Override
        public int getToIndex() {
            return 0;
        }
    }

    final class UpdateChange<T> extends Change<T> {
        public UpdateChange(List<T> list) {
            super(list);
        }

        @Override
        public boolean replaced() {
            return true;
        }

        @Override
        public List<T> getRemoved() {
            return null;
        }

        @Override
        public int getFromIndex() {
            return 0;
        }

        @Override
        public int getToIndex() {
            return 0;
        }
    }

    final class RemovedChange<T> extends Change<T> {

        private int from, to;

        private List<T> list;

        public RemovedChange(int from, int to, List<T> list) {
            super(list);
            this.from = from;
            this.to = to;
            this.list = list;
        }

        @Override
        public boolean removed() {
            return true;
        }

        @Override
        public List<T> getRemoved() {
            return list.subList(from, to);
        }

        @Override
        public int getFromIndex() {
            return from;
        }

        @Override
        public int getToIndex() {
            return to;
        }
    }

    final class SingleChange<T> extends Change<T> {
        public SingleChange(List<T> list) {
            super(list);
        }

        @Override
        public List<T> getRemoved() {
            return null;
        }

        @Override
        public int getFromIndex() {
            return 0;
        }

        @Override
        public int getToIndex() {
            return 0;
        }
    }
}
