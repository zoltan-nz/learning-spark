package nz.zoltan;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

/**
 * Hello world!
 */
public class SimpleApp {

    // A sample big text file location
    public static final String BIG_TEXT_FILE_LOCATION = "SimpleApp/src/main/resources/sample-text/toldi.txt";

    public static void main(String[] args) {

        // Launching a Spark instance
        SparkSession spark = SparkSession
                .builder()
                .appName("Simple Application")
                .config("spark.master", "local")
                .getOrCreate();

        // Reading the file from the file system
        Dataset<String> bigTextFileByLine = spark.read().textFile(BIG_TEXT_FILE_LOCATION).cache();

        // Basic analysis
        long numAs = bigTextFileByLine.filter((FilterFunction<String>) line -> line.contains("a")).count();
        long numBs = bigTextFileByLine.filter((FilterFunction<String>) line -> line.contains("b")).count();
        long numToldis = bigTextFileByLine.filter((FilterFunction<String>) line -> line.contains("Toldi")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs + ", lines with Toldi: " + numToldis);

        spark.stop();
    }
}
