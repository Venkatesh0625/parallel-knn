package com.processors;

import com.pdc.Data;
import com.pdc.Pair;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class KNNDistanceProcessor extends Thread {
    private List<Data> trainData = new ArrayList<Data>();
    private Data point;
    private BlockingQueue blockingQueue;

    public KNNDistanceProcessor(BlockingQueue q, List<Data> trainData, Data point) {
        this.trainData = trainData;
        this.blockingQueue = q;
        this.point = point;
    }

    @Override
    public void run() {
        trainData.forEach((train) -> {
            double dist = Math.sqrt(Math.pow(train.petalLength - point.petalLength, 2)
                    + Math.pow(train.petalWidth - point.petalWidth, 2)
                    + Math.pow(train.sepalLength - point.sepalLength, 2)
                    + Math.pow(train.sepalWidth - point.sepalWidth, 2));
            try {
                this.blockingQueue.put(new Pair(train, dist));
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }
        });
    }
}
