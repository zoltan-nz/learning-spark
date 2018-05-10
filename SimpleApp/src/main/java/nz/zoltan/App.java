package nz.zoltan;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        String logFile = "SimpleApp/src/main/resources/sample-text/toldi.txt"; // Should be some file on your system
        SparkSession spark = SparkSession
                .builder()
                .appName("Simple Application")
                .config("spark.master", "local")
                .getOrCreate();
        Dataset<String> logData = spark.read().textFile(logFile).cache();

        long numAs = logData.filter((FilterFunction<String>) s -> s.contains("a")).count();
        long numBs = logData.filter((FilterFunction<String>) s -> s.contains("b")).count();
        long numToldis = logData.filter((FilterFunction<String>) s -> s.contains("Toldi")).count();


        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs + ", lines with Toldi: " + numToldis);

        spark.stop();
    }
}
