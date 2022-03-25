package com.pdc;

import com.processors.KNNDistanceProcessor;
import com.processors.KNNSortingProcessor;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class KNN {
    private final int k;
    private List<Data> trainData = new ArrayList<>();
    private List<Data> testData = new ArrayList<>();
    public BlockingQueue<Pair> q = new LinkedBlockingQueue<>();

    public KNN(int k) {
        this.k = k;
    }

    public void loadData(String fileName) {
        File dataFile;
        List<Data> dataset = new ArrayList<>();
        try {
            dataFile = new File(fileName);
            Scanner scanner = new Scanner(dataFile);
            scanner.useDelimiter("\n");

            scanner.next();
            while(scanner.hasNext()) {
                String row = scanner.next();
                var attr = row.split(",");

                float sepalLength = Float.parseFloat(attr[0]);
                float sepalWidth = Float.parseFloat(attr[1]);
                float petalLength = Float.parseFloat(attr[2]);
                float petalWidth = Float.parseFloat(attr[3]);
                String variety = attr[4];

                dataset.add(new Data(sepalLength, sepalWidth, petalLength, petalWidth, variety));
            }

            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println("error" + e);
        }

        int size = dataset.size();

        Collections.shuffle(dataset);

        int trainSize = (int) (0.7 * size);
        trainData = dataset.subList(0, trainSize);
        testData = dataset.subList(trainSize, size);

        System.out.println("Loaded " + (trainData.size() + testData.size()) + " rows");
    }

    public void classify() {
        long start, end;
        start = Calendar.getInstance().getTimeInMillis();
        List<Data> result = new ArrayList<>();

//        testData.forEach();
        testData.parallelStream().forEach((testPoint) -> {
            BlockingQueue<Pair> q = new LinkedBlockingQueue<>();
            KNNDistanceProcessor knnDistance = new KNNDistanceProcessor(q, trainData, testPoint);
//            knnDistance.join()
            knnDistance.start();

            KNNSortingProcessor knnSorting = new KNNSortingProcessor(q, trainData.size(), this.k);
//            knnSorting.run()
            knnSorting.start();

            try {
                knnDistance.join();
                knnSorting.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Data t = new Data(testPoint.sepalLength, testPoint.sepalWidth, testPoint.petalLength, testPoint.petalWidth, "");
            t.variety = knnSorting.FinalLabel;
            result.add(t);
        });

        end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Time Parallel: " + (end - start));

        result.forEach((r) -> {
            //System.out.println(r.sepalLength + "," + r.sepalWidth + "," + r.petalLength + "," + r.petalWidth + "," + r.variety);
        });
    }

    public void classifySerial() {
        long start, end;
        start = Calendar.getInstance().getTimeInMillis();
        List<Data> result = new ArrayList<>();

        testData.forEach((testPoint) -> {
            BlockingQueue<Pair> q = new LinkedBlockingQueue<>();
            KNNDistanceProcessor knnDistance = new KNNDistanceProcessor(q, trainData, testPoint);
            knnDistance.run();

            KNNSortingProcessor knnSorting = new KNNSortingProcessor(q, trainData.size(), this.k);
            knnSorting.run();

            Data t = new Data(testPoint.sepalLength, testPoint.sepalWidth, testPoint.petalLength, testPoint.petalWidth, "");
            t.variety = knnSorting.FinalLabel;
            result.add(t);
        });

        end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Time Serial: " + (end - start));

        result.forEach((r) -> {
            //System.out.println(r.sepalLength + "," + r.sepalWidth + "," + r.petalLength + "," + r.petalWidth + "," + r.variety);
        });
    }
}
