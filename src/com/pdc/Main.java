package com.pdc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {
        KNN knn =  new KNN(5);
        knn.loadData("C:\\Users\\venka\\Intellij\\knn-parallel\\src\\com\\pdc\\iris.csv");
//        knn.classify();
        knn.classifySerial();
        knn.classify();
    }
}
