package com.processors;

import com.pdc.Data;
import com.pdc.Pair;
import com.utils.Helper;

import java.util.*;
import java.util.concurrent.*;

public class KNNSortingProcessor extends Thread {
    public int k;
    public BlockingQueue blockingQueue;
    public Pair [] sortedDistance;
    public int countToWait;
    public String FinalLabel = "";

    public KNNSortingProcessor(BlockingQueue q, int countToWait, int k) {
        this.blockingQueue = q;
        this.countToWait = countToWait;
        this.k = k;
    }

    @Override
    public void run() {
        int itr = 0;
        double [] distArray = new double[countToWait];
        sortedDistance = new Pair[countToWait];
        Arrays.fill(distArray, 0);
        Arrays.fill(sortedDistance, new Pair(new Data(0, 0, 0, 0, ""), 0));

        while(itr < countToWait) {
            Pair pair = null;
            try {
                pair = (Pair) blockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(pair != null) {
                Helper.binaryInsertionSort(distArray, sortedDistance, pair, itr);
            }

            itr++;
        }

//        System.out.println("\n" + sortedDistance.size() + "\t" + sortedDistance.get(0).distance);


        List<String> labels = new ArrayList<>();

        List<Pair> sortedDistanceList = Arrays.asList(sortedDistance);

        sortedDistanceList.subList(0, k).forEach((d) -> {
            labels.add(d.data.variety);
        });

        FinalLabel = Helper.mostFrequent(labels);
    }
}
