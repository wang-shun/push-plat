package com.lvmama.bms.scheduler.support.assign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 通过模拟退火算法，将N个数据分成M组，使得各组数据的数值和最平均，即各组的均方差最小
 */
public class Anneal {

    public static <T extends AnnealData> List<List<T>> calculate(List<T> dataList, int groupCount, int iterate) {

        double minMeanSquare = 0;
        int[] minPosition = null;

        for(int i = 0; i < iterate; i++) {

            double[] sum = new double[groupCount];
            int[] position = new int[dataList.size()];
            Random random = new Random();
            for(int index = 0; index < dataList.size(); index++) {
                position[index] = random.nextInt(groupCount);
                sum[position[index]] += dataList.get(index).getData();
            }

            double delta = 0.98;
            double eps = 1e-8;

            for(double t = 2000; t > eps; t*=delta) {

                random = new Random();
                int dataIndex = random.nextInt(dataList.size());
                int randomGroup = position[dataIndex];
                int minGroup = getMinSum(sum);
                if(randomGroup == minGroup) {
                    continue;
                }

                double prevMeanSquare = getMeanSquare(sum);
                double currentMeanSquare = prevMeanSquare;
                sum[randomGroup]-=dataList.get(dataIndex).getData();
                sum[minGroup]+=dataList.get(dataIndex).getData();

                double nextMeanSquare = getMeanSquare(sum);
                if(nextMeanSquare <= prevMeanSquare) {
                    position[dataIndex] = minGroup;
                    currentMeanSquare = nextMeanSquare;
                } else {
                    sum[randomGroup]+=dataList.get(dataIndex).getData();
                    sum[minGroup]-=dataList.get(dataIndex).getData();
                }

                if(i == 0) {
                    minMeanSquare = currentMeanSquare;
                    minPosition = position;
                } else if(minMeanSquare > currentMeanSquare) {
                    minMeanSquare = currentMeanSquare;
                    minPosition = position;
                }

            }

        }

        List<List<T>> dataGroups = new ArrayList<>();

        for(int m = 0; m < groupCount; m++) {
            double sum = 0;
            List<T> dataGroup = new ArrayList<>();
            for(int n = 0; n < minPosition.length; n++) {
                if(minPosition[n] == m) {
                    dataGroup.add(dataList.get(n));
                    sum+=dataList.get(n).getData();
                }
            }
            dataGroups.add(dataGroup);
        }

        return dataGroups;

    }

    private static double getMeanSquare(double[] sum) {

        double average = 0;
        for(int index = 0; index < sum.length; index++) {
            average+=sum[index];
        }
        average /= sum.length;

        double meanSquare = 0;
        for(int index = 0; index < sum.length; index++) {
            meanSquare+= Math.pow(sum[index] - average, 2);
        }

        return meanSquare;

    }

    private static int getMinSum(double[] sum) {

        int index = 0;
        for(int i = 1; i < sum.length; i++) {
            if(sum[index] > sum[i]) {
                index = i;
            }
        }

        return index;
    }

    public static void main_(String[] args) {

        List<AnnealData> dataList = new ArrayList<>();//Arrays.asList(1.1d, 2.3d, 3.5d, 4.6d, 5.1d, 6.1d, 7.1d, 8d, 9d, 10d);
        dataList.add(new Task("1", 1.1d, null));
        dataList.add(new Task("2", 2.3d, null));
        dataList.add(new Task("3", 3.5d, null));
        dataList.add(new Task("4", 4.6d, null));
        dataList.add(new Task("5", 5.1d, null));
        dataList.add(new Task("6", 6.1d, null));
        dataList.add(new Task("7", 7.1d, null));
        dataList.add(new Task("8", 8d, null));
        dataList.add(new Task("9", 9d, null));
        dataList.add(new Task("10", 10d, null));


        int quantity = 3;

        List<List<AnnealData>> dataGroup = Anneal.calculate(dataList, quantity, 100);

        System.out.println(dataGroup);

    }


}
