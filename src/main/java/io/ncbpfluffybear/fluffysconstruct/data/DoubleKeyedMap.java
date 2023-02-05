package io.ncbpfluffybear.fluffysconstruct.data;

import java.util.HashMap;

/**
 * Allows access of value V with either K or L.
 * Each entry of K and L also stores the alternate key in
 * a Pair, with V as the second value.
 */
public class DoubleKeyedMap<K, L, V> {

    // Two synchronized maps that refer to each other
    private final HashMap<K, Pair<L, V>> firstMap; // TODO Players can be multiple
    private final HashMap<L, Pair<K, V>> secondMap;

    public DoubleKeyedMap() {
        this.firstMap = new HashMap<>();
        this.secondMap = new HashMap<>();
    }

    public void put(K firstKey, L secondKey, V value) {
        this.firstMap.put(firstKey, new Pair<>(secondKey, value));
        this.secondMap.put(secondKey, new Pair<>(firstKey, value));
    }

    public boolean removeFirst(K firstKey) {
        if (!this.firstMap.containsKey(firstKey)) {
            return false;
        }

        this.secondMap.remove(this.firstMap.get(firstKey).getFirst());
        this.firstMap.remove(firstKey);
        return true;
    }

    public boolean removeSecond(L secondKey) {
        if (!this.secondMap.containsKey(secondKey)) {
            return false;
        }

        this.firstMap.remove(this.secondMap.get(secondKey).getFirst());
        this.secondMap.remove(secondKey);
        return true;
    }

    public V getFirst(K firstKey) {
        return this.firstMap.get(firstKey).getSecond();
    }

    public V getSecond(L secondKey) {
        return this.secondMap.get(secondKey).getSecond();
    }

    public boolean hasFirst(K firstKey) {
        return this.firstMap.containsKey(firstKey);
    }

    public boolean hasSecond(L secondKey) {
        return this.secondMap.containsKey(secondKey);
    }

}
