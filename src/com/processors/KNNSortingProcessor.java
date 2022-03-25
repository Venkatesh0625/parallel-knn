package com.processors;

import com.pdc.Data;
import com.pdc.Pair;
import utils.Helper;

import java.util.*;
import java.util.concurrent.*;

public class KNNSortingProcessor extends Thread {
    public int k;
    public BlockingQueue blockingQueue;
    public List<Pair> sortedDistance = new ArrayList<Pair>();
    public int countToWait;
    public String FinalLabel = "";

    public KNNSortingProcessor(BlockingQueue q, int countToWait, int k) {
        this.blockingQueue = q;
        this.countToWait = countToWait;
        this.k = k;
    }

    @Override
    public void run() {
        int count = 0;
        while(count < countToWait) {

            Pair pair = null;
            try {
                pair = (Pair) blockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.sortedDistance.add(pair);
            count++;
        }


        Collections.sort(sortedDistance, new Comparator<Pair>() {
            public int compare(Pair p1, Pair p2) {
                return p1.compareTo(p2);
            }
        });

//        System.out.println("\n" + sortedDistance.size() + "\t" + sortedDistance.get(0).distance);


        List<String> labels = new ArrayList<>();

        sortedDistance.subList(0, k).forEach((d) -> {
            labels.add(d.data.variety);
        });

        FinalLabel = Helper.mostFrequent(labels);
    }
}
