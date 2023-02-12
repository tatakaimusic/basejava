package com.urise.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainHW12 {
    public static void main(String[] args) {
        int[] values = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(values));


        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(oddOrEven(list));
    }

    static int minValue(int[] values) {
        int result = 0;
        int[] array = IntStream.of(values).distinct().sorted().toArray();
        for (int i = 0; i < array.length; i++) {
            result *= 10;
            result += array[i];
        }
        return result;
    }

//    static List<Integer> oddOrEven(List<Integer> integers) {
//        List<Integer> list = new ArrayList<>();
//        if (integers.stream().mapToInt(i -> i).sum() % 2 != 0) {
//            integers.forEach(integer -> {
//                if (integer % 2 == 0) {
//                    list.add(integer);
//                }
//            });
//        } else {
//            integers.forEach(integer -> {
//                if (integer % 2 != 0) {
//                    list.add(integer);
//                }
//            });
//        }
//        return list;
//    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream().collect(Collectors.partitioningBy(s -> s % 2 == 0));
        if (map.get(false).size() % 2 == 0) {
            return map.get(true);
        }
        return map.get(false);
    }

}



