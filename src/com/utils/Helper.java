package com.utils;

import com.pdc.Data;
import com.pdc.Pair;

import java.util.*;

public class Helper {

    public static void binaryInsertionSort(double distArray[], Pair sortedDistance[], Pair p, int i)
    {
        double dist = p.distance;
        Data d = p.data;

        int j = Math.abs(Arrays.binarySearch(distArray, 0, i, dist) + 1);

        System.arraycopy(distArray, j, distArray, j + 1, i - j);
        System.arraycopy(sortedDistance, j, sortedDistance, j + 1, i - j);

        distArray[j] = dist;
        sortedDistance[j] = p;
    }

    public static String mostFrequent(List<String> l)
    {
        int n = l.size();

        Map<String, Integer> hp = new HashMap<>();
        for(int i = 0; i < n; i++)
        {
            String key = l.get(i);
            if(hp.containsKey(key))
                hp.put(key, hp.get(key) + 1);
            else
                hp.put(key, 1);
        }

        // find max frequency.
        int max_count = 0;
        String res = "";

        for(Map.Entry<String, Integer> val : hp.entrySet())
            if (max_count < val.getValue())
            {
                res = val.getKey();
                max_count = val.getValue();
            }


        return res;
    }
}

