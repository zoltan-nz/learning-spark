# Playing with Apache Spark

## First Step: Running Spark development environment

Start with Quick Start from the official documentation: https://spark.apache.org/docs/latest/quick-start.html

Let's implement the Self-Contained Application.

Source: https://spark.apache.org/docs/latest/quick-start.html#self-contained-applications

1. Create a new Maven project.
2. Add the code from the documentation.
3. Save a huge sample text in your `resources` folder.

**Issue #1**: The code will not work without a little change.

We have to use type casting in the lambda function.

`(FilterFunction<String>)`

**Issue #2**: It is much easier to debug your code if you run Spark server in local mode.

`.config("spark.master", "local")`

The right code:

```java
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
```

More details about single developer mode: https://stackoverflow.com/questions/38008330/spark-error-a-master-url-must-be-set-in-your-configuration-when-submitting-a

**Master URLs**: https://spark.apache.org/docs/latest/submitting-applications.html#master-urls

## Second Step: Using RDD

Read this: https://spark.apache.org/docs/latest/rdd-programming-guide.html

More details in [`RDDApp`](RDDApp/src/main/java/nz/zoltan/RDDApp.java)


## Notes

* Using multi module maven structure. More information about building a multi module maven project: https://books.sonatype.com/mvnex-book/reference/multimodule.html