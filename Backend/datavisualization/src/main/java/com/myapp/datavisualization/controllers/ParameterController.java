package com.myapp.datavisualization.controllers;



import com.myapp.datavisualization.algorithm.PrefixSpanPattern;
import com.myapp.datavisualization.algorithm.TranscriptionCluster;
import com.myapp.datavisualization.models.Para;
import com.myapp.datavisualization.services.ParameterService;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
//@RequestMapping(value="/algorithm/parameters")
//@CrossOrigin(origins = "http://xx-domain.com", maxAge = 3600)
public class ParameterController {
//    private ParameterService parameterService;
//    @Autowired
//    public ParameterController(ParameterService parameterService) {
//
//        // Dependency managed by Spring
//        this.parameterService = parameterService;
//    }
    //*********************************************************
     //post方法不知道怎么回事，怎么都不行
//    @RequestMapping(value = "/test",method = RequestMethod.POST)
//    public String testRequst() {
//        //System.out.println(para.getALPHABET_SIZE());
//        return "success";
//
//    }
////*****************************************************************

        @RequestMapping(value = "/algorithm/parameters/PrefixSpanGetPatterns", method = RequestMethod.GET)
        @CrossOrigin
        public String PatternCal(@RequestParam(value = "MinSupport", required = true) float MinSupport, @RequestParam(value = "MaxPatternLength", required = true) float MaxPatternLength, @RequestParam(value = "Length", required = true) String Length, @RequestParam(value = "NumberOfPatterns", required = true) String NumberOfPatterns)
        {
            //保存返回值的变量，具体以后什么类型，什么名字，什么方式返回之后整理算法的时候再进行调整。
            List<String> PatternResult;
            Para para=new Para();
            String s="";
            List<String> lenPattern = new ArrayList<String>();
            int length=0;
            int numberOfPatterns=0;
            int i=0;


            PrefixSpanPattern prefixSpanPattern=new PrefixSpanPattern(MinSupport,MaxPatternLength);
            PatternResult = prefixSpanPattern.getPattern();

            if(Length!="all"){
                length = Integer.parseInt(Length);
            }
            if(NumberOfPatterns!="all"){
                numberOfPatterns = Integer.parseInt(NumberOfPatterns);

            }

             //get specific length of patterns
            for(String pattern : PatternResult)    {
                String str[] = pattern.split(",");
                int l=str.length;
                if(l==length+1){
                    lenPattern.add(pattern);

//                s+=pattern;
                }

            }

            //preprocess for later sort
            int lenOfSetPattern=lenPattern.size();
            final String[][] strPattern=new String[lenOfSetPattern][length+1];
            for(String lenP:lenPattern){
                String strlen[] = lenP.split(",");
                for(int j=0;j<length+1;j++){
                            strPattern[i][j]=strlen[j];

                       }
                       i++;

            }


            //sort by frequency
            final int len=length;
            Arrays.sort(strPattern, new Comparator<String[]>() {
                @Override
                public int compare(final String[] entry1, final String[] entry2) {
                    final String time1 = entry1[len];
                    final String time2 = entry2[len];
                    return time2.compareTo(time1);
                }
            });
            //get specific number of pattern

            for (int m=0;m<numberOfPatterns;m++){
//

            String numPattern=StringUtils.join(strPattern[m], ",");

                s+=numPattern;
            }

            for (final String[] m : strPattern) {
                System.out.println(m[0] + " " + m[1]+" "+m[2]);
            }



            return s;


        }
        @RequestMapping(value = "/algorithm/parameters/getclusterspatterns", method = RequestMethod.GET)
        @CrossOrigin
        public String ClusterCal(@RequestParam(value = "K", required = true) int K, @RequestParam(value = "MaxIterations", required = true) int MaxIterations, @RequestParam(value = "Runs", required = true) int Runs)
        {
            //保存返回值的变量，具体以后什么类型，什么名字，什么方式返回之后整理算法的时候再进行调整。
//            JavaPairRDD<Integer, String>  ClusterResult;
            TranscriptionCluster transcriptionCluster=new TranscriptionCluster(K,MaxIterations,Runs);
            transcriptionCluster.getCluster();
//            System.out.println(ClusterMinSupport);
//            System.out.println(ClusterMaxPatternLength);
//            System.out.println(K);
//            System.out.println(MaxIterations);
//            System.out.println(Runs);

            System.out.println("聚类成功");
//            return ClusterResult;
            return "juleichenggong";

        }


}
