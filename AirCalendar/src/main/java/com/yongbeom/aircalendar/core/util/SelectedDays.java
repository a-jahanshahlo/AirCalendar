package com.yongbeom.aircalendar.core.util;

import java.io.Serializable;

public   class SelectedDays<K> implements Serializable {
    private static final long serialVersionUID = 3942549765282708376L;
    private K first;
    private K last;

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public K getLast() {
        return last;
    }

    public void setLast(K last) {
        this.last = last;
    }
}
