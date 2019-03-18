package com.myapp.datavisualization.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// $example off$
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
// $example on$
import org.apache.spark.mllib.fpm.PrefixSpan;
import org.apache.spark.mllib.fpm.PrefixSpanModel;
// $example off$
import org.apache.spark.SparkConf;

public class PrefixSpanPattern {
    private float MinSupport;
    private float MaxPatternLength;

    public PrefixSpanPattern(float MinSupport, float MaxPatternLength) {

        this.MinSupport=MinSupport ;
        this.MaxPatternLength=MaxPatternLength;

    }

    public List<String> getPattern() {

        SparkConf sparkConf = new SparkConf().setAppName("PrefixSpan").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        GetData getData=new GetData("jdbc:db2://steen.informatik.rwth-aachen.de:50020/mobsos","stnV95DB","stdb2v95","com.ibm.db2.jcc.DB2Driver","select SESSION_ID,replace(replace(xml2clob(xmlagg(xmlelement(NAME a, METHOD_NAME||','))),'<A>',''),'</A>',' ') FROM\n" +
                  "mobsos.MONITORING where METHOD_NAME is not null and METHOD_NAME!='instantiateContext' and METHOD_NAME!='continueConnection' and METHOD_NAME!='testConnection' GROUP BY SESSION_ID");

        //information for noracle database
        //GetData getDataNoracle=new GetData("jdbc:mysql://las2peer.dbis.rwth-aachen.de:3306/NORACLEMON","KUTUfano77","NORACLERO","com.mysql.jdbc.Driver","select group_concat(EVENT) as e FROM MESSAGE group by SOURCE_AGENT");


        // DATA FROM MOBSOS
       List<String> fileContent=getData.GetDataFromDataBase();

        //DATA FROM NORACLE
       //  List<String> fileContent=getDataNoracle.GetDataFromDataBase();

        //transfer data to 3 dimension data
        List<List<List<String>>> SessionList=new ArrayList<>();
        for(int i=0;i<fileContent.size();i++){
            List<List<String>> subSessionList=new ArrayList<>();
            //transfer each string to String[]
            String str[]=fileContent.get(i).split(",");
            //transfer each line to list
            List<String> lineList=new ArrayList<String>();
            lineList=Arrays.asList(str);
            for(int j=1;j<lineList.size();j++){
                List<String> tempList=new ArrayList<>();
                if(lineList.get(j).equals("continueConnection"))
                    continue;
                if(lineList.get(j).equals("instantiateContext"))
                    continue;
                tempList.add(lineList.get(j));
                subSessionList.add(tempList);


            }
            SessionList.add(subSessionList);
        }
        // $example on$
        JavaRDD<List<List<String>>> sequences = sc.parallelize(SessionList);
        List<String> Preresult=new ArrayList<>();
        List<String> result=new ArrayList<>();

        //beagin to count time
        long startTime = System.currentTimeMillis();


        /*MinSupport: The threshold is supported. The size of the value affects the set size of the last frequent item set.
        *The larger the support threshold, the less the last frequent item set is 0.05.
        *
        * MaxPatternLength: The length of the longest frequent itemset, the smaller the number of last frequent itemsets: 10
        */
        PrefixSpan prefixSpan = new PrefixSpan()
                .setMinSupport(MinSupport)
                .setMaxPatternLength((int)MaxPatternLength);
        PrefixSpanModel<String> model = prefixSpan.run(sequences);



        //return result
        for (PrefixSpan.FreqSequence<String> freqSeq: model.freqSequences().toJavaRDD().collect()) {
            //把最后的pattern存入list
            result.add(freqSeq.javaSequence() + ", " + freqSeq.freq()+"\r\n");

        }
        //time based
        long endTime = System.currentTimeMillis();    //get end time
        sc.stop();
        return result;
    }
    public  List<String> getPatternForCluster(List<String> fileContent){
        SparkConf sparkConf = new SparkConf().setAppName("PrefixSpan2").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        List<List<List<String>>> SessionList=new ArrayList<>();
        for(int i=0;i<fileContent.size();i++){
            List<List<String>> subSessionList=new ArrayList<>();
            //transfer each string to String[]
            String str[]=fileContent.get(i).split(",");
            //transfer each line to list
            List<String> lineList=new ArrayList<String>();
            lineList=Arrays.asList(str);
            for(int j=1;j<lineList.size();j++){
                List<String> tempList=new ArrayList<>();
                if(lineList.get(j).equals("continueConnection"))
                    continue;
                if(lineList.get(j).equals("instantiateContext"))
                    continue;
                tempList.add(lineList.get(j));
                subSessionList.add(tempList);


            }
            SessionList.add(subSessionList);
        }
        // $example on$
        JavaRDD<List<List<String>>> sequences = sc.parallelize(SessionList);
        List<String> Preresult=new ArrayList<>();
        List<String> result=new ArrayList<>();

        //beagin count time
        long startTime = System.currentTimeMillis();

        PrefixSpan prefixSpan = new PrefixSpan()
                .setMinSupport(MinSupport)
                .setMaxPatternLength((int)MaxPatternLength);
        PrefixSpanModel<String> model = prefixSpan.run(sequences);



        //return result
        for (PrefixSpan.FreqSequence<String> freqSeq: model.freqSequences().toJavaRDD().collect()) {
            result.add(freqSeq.javaSequence() + ", " + freqSeq.freq()+"\r\n");
        }
        //end time
        long endTime = System.currentTimeMillis();

        System.out.println("runtime：" + (endTime - startTime) + "ms");

        // $example off$
        sc.stop();
        return result;

    }
}