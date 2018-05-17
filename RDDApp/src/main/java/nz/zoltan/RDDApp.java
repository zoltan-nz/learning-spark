package nz.zoltan;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nz.zoltan.Constants.BIG_TEXT_FILE_LOCATION;

public class RDDApp {

    private static final String APP_NAME = "RDD App";
    private static final String MASTER = "local[8]";

    public static void main(String[] args) throws InterruptedException {


        SparkConf sparkConf = new SparkConf()
                .setAppName(APP_NAME)
                .setMaster(MASTER);

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        // Aggregate a simple series of integer
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data).persist(StorageLevel.MEMORY_ONLY());
        int sum = distData.reduce((a, b) -> a + b);

        // Print out each element using collect() or take();
        distData.collect().forEach(i -> System.out.println("item: " + i));
        distData.take(2).forEach(i -> System.out.println("item: " + i));

        System.out.println("distData.reduce: " + sum);


        // Aggregate the length of lines in a text file
        JavaRDD<String> lines = sc.textFile(BIG_TEXT_FILE_LOCATION).cache();
        JavaRDD<Integer> lineLengths = lines
                .map(String::length)
                .persist(StorageLevel.MEMORY_AND_DISK());

        int totalLength = lineLengths.reduce((a, b) -> a + b);

        System.out.println("lineLengths.reduce: " + totalLength);


        // Using key-value pairs
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        JavaPairRDD<String, Integer> wordsWithOne = words.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> wordsWithCount = wordsWithOne.reduceByKey((a, b) -> a + b);
        JavaPairRDD<Integer, String> countsWithWord = wordsWithCount.mapToPair(Tuple2::swap);
        JavaPairRDD<Integer, String> sortedCounts = countsWithWord.sortByKey();

        sortedCounts.collect().forEach((tuple) -> System.out.println(tuple._2 + ": " + tuple._1));


        // Using Accumulator
        LongAccumulator accum = sc.sc().longAccumulator();
        List<Integer> numbers = IntStream.range(1, 10000000).boxed().collect(Collectors.toList());
        sc.parallelize(numbers).foreach(accum::add);

        System.out.println("accum.value():" + accum.value());

        sc.stop();
    }
}
