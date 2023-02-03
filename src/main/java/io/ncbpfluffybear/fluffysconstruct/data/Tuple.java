package io.ncbpfluffybear.fluffysconstruct.data;

public class Tuple<K, V, X> {

    private final K first;
    private final V second;
    private final X third;

    public Tuple(K first, V second, X third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public X getThird() {
        return third;
    }
}
