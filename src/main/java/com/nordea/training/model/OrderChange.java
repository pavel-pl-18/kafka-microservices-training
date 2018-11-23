package com.nordea.training.model;

import java.time.LocalDateTime;

public class OrderChange {
    private Order old;
    private Order current;
    private LocalDateTime ts;

    public Order getCurrent() {
        return current;
    }

    public void setCurrent(Order current) {
        this.current = current;
    }

    public LocalDateTime getTs() {
        return ts;
    }

    public void setTs(LocalDateTime ts) {
        this.ts = ts;
    }

    public Order getOld() {
        return old;
    }

    public void setOld(Order old) {
        this.old = old;
    }

    @Override
    public String toString() {
        return "OrderChange{" +
                "old=" + old +
                ", current=" + current +
                ", ts=" + ts +
                '}';
    }
}
