package com.myapp.datavisualization.algorithm;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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



//        String path="/Users/Jia/Desktop/PsessionSave.txt";
//        //transfer data to list
//        List<String> fileContent=ReaderFileLine.getFileContent(path);
       GetData getData=new GetData("jdbc:db2://steen.informatik.rwth-aachen.de:50020/mobsos","stnV95DB","stdb2v95","com.ibm.db2.jcc.DB2Driver","select SESSION_ID,replace(replace(xml2clob(xmlagg(xmlelement(NAME a, METHOD_NAME||','))),'<A>',''),'</A>',' ') FROM\n" +
                "mobsos.MONITORING where METHOD_NAME is not null and METHOD_NAME!='instantiateContext' and METHOD_NAME!='continueConnection' and METHOD_NAME!='testConnection' GROUP BY SESSION_ID");
//      GetData getDataNoracle=new GetData("jdbc:mysql://las2peer.dbis.rwth-aachen.de:3306/NORACLEMON","KUTUfano77","NORACLERO","com.mysql.jdbc.Driver","select group_concat(EVENT) as e FROM MESSAGE group by SOURCE_AGENT");


        // DATA FROM MOBSOS
        List<String> fileContent=getData.GetDataFromDataBase();

        //DATA FROM NORACLE
//       List<String> fileContent=getDataNoracle.GetDataFromDataBase();
//        System.out.println("获取数据库的数据"+fileContent);

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

        //开始时间计时
        long startTime = System.currentTimeMillis();

        PrefixSpan prefixSpan = new PrefixSpan()
                .setMinSupport(MinSupport) //支持阈值，他的取值大小影响最后的频繁项集的集合大小，支持阈值越大，最后的频繁项集数越少0.05
                .setMaxPatternLength((int)MaxPatternLength); //最长的频繁项集的长度，越小，最后的频繁项集的数量越少 10
        PrefixSpanModel<String> model = prefixSpan.run(sequences);



        //return result
        for (PrefixSpan.FreqSequence<String> freqSeq: model.freqSequences().toJavaRDD().collect()) {
            //把最后的pattern存入list
//            if(!freqSeq.javaSequence().isEmpty()){
//                //read the pattern result into list
            result.add(freqSeq.javaSequence() + ", " + freqSeq.freq()+"\r\n");
//
//            }
//            freqSeq.javaSequence().removeAll(Collections.singleton("［ ］"));


//            String s = freqSeq.javaSequence() + ", ";
//            result.add(s.substring(1) + freqSeq.freq());

//            System.out.println(freqSeq.javaSequence() + ", " + freqSeq.freq());
        }
        //时间计时结束
        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

        // $example off$

//        result.removeAll(Collections.singleton(",［[]］"));
        sc.stop();
        return result;
    }
}