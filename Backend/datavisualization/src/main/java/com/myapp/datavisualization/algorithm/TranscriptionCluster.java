package com.myapp.datavisualization.algorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import scala.Tuple2;

public class TranscriptionCluster {
    //暂时return void, 之后要返回数据or图片
    private int K;
    private int MaxIterations;
    private int Runs;

    public TranscriptionCluster(int K,int MaxIterations,int Runs) {

        this.K=K;
        this.MaxIterations=MaxIterations;
        this.Runs=Runs;
    }

    public void getCluster() {
        // Create a Java Spark Context
        SparkConf conf = new SparkConf().setAppName("KMeans Example")
                .setMaster("local[2]");// uncomment this if you are running on a

        // locally
        JavaSparkContext sc = new JavaSparkContext(conf);
        final HashingTF tf = new HashingTF(10000);
//        String fileName = "/Users/Jia/Desktop/PsessionSave.txt";

        GetData getData=new GetData();
        List<String> fileContent=getData.GetDataFromDataBase();
//        System.out.println("获取数据库的数据"+fileContent);
        JavaRDD<String> list2data= sc.parallelize(fileContent);
        //根据数据特征进行数据处理
//        JavaPairRDD<String, Vector> data = sc.textFile(fileName)
        JavaPairRDD<String, Vector> data = list2data
                .mapToPair(new PairFunction<String, String, Vector>() {
                    public Tuple2<String, Vector> call(String in)
                            throws Exception {
                        String line = in;
//
                        String text = line.replace(",", " ");
//                        System.out.println(text);
                        // Adding string tokens as feature vectors
                        List<String> annotations = new ArrayList<String>();
                        annotations.addAll(Arrays.asList(text.split(" ")));
                        // Transform to a vector with tf as weights
                        // 输入数据是list of each line, also means each line is a kind of document
                        Vector entities = tf.transform(annotations);
                        return new Tuple2<String, Vector>(line, entities);
                    }
                }).cache();

        JavaRDD<Vector> points = data.values();
        final IDFModel idf = new IDF().fit(points);
        // Transform to a vector with tfidf as weights
        JavaRDD<Vector> idfVectors = idf.transform(points).cache();
        // Run Kmeans with K as 100, 300 iterations,  best of two runs, parallel kmeans++ to initialize centroids
        final KMeansModel model = KMeans.train(idfVectors.rdd(), K,MaxIterations, Runs,
                KMeans.K_MEANS_PARALLEL());

        // Predict the clusters and write the output to a file
        JavaPairRDD<Integer, String> clusteredData = data.mapToPair(
                new PairFunction<Tuple2<String, Vector>, Integer, String>() {
                    public Tuple2<Integer, String> call(Tuple2<String, Vector> t)
                            throws Exception {
                        String json = t._1; //string data of tuple
                        Vector v = t._2;  //vector data of tuple
                        Vector idfvec = idf.transform(v);
                        int cluster = model.predict(idfvec);


                        return new Tuple2<Integer, String>(cluster, json);
                    }

                }).cache();
//        clusteredData.groupByKey().saveAsTextFile(
//                "clusterData/Session");

        double cost = model.computeCost(points.rdd());
        System.out.println("Cost: " + cost);
        sc.close();
//        return clusteredData;
    }
}
