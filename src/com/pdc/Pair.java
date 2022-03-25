package com.pdc;

public class Pair implements Comparable<Pair> {
    public Data data;
    public double distance;

    public Pair(Data data, double distance) {
        this.data = data;
        this.distance = distance;
    }

    @Override
    public int compareTo(Pair p) {
        if (this.distance > p.distance) return 1;
        else if (this.distance < p.distance) return -1;
        else return 0;
    }
}
