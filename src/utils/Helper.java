package utils;

import java.util.*;

public class Helper {

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

