package nz.zoltan;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

import static nz.zoltan.Constants.BIG_TEXT_FILE_LOCATION;

public class RDDApp {

    private static final String APP_NAME = "RDD App";
    private static final String MASTER = "local";

    public static void main(String[] args) throws InterruptedException {


        SparkConf sparkConf = new SparkConf()
                .setAppName(APP_NAME)
                .setMaster(MASTER);

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        // Aggregate a simple series of integer
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data);
        Integer sum = distData.reduce((a,b) -> a + b);

        System.out.println("distData.reduce: " + sum);

        // Aggregate the length of lines in a text file
        JavaRDD<String> lines = sc.textFile(BIG_TEXT_FILE_LOCATION);

        sc.stop();
    }
}
