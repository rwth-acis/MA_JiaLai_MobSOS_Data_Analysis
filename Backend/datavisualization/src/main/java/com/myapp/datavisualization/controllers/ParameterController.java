package com.myapp.datavisualization.controllers;


import com.myapp.datavisualization.algorithm.PatternDiscovery;
import com.myapp.datavisualization.algorithm.PrefixSpanPattern;
import com.myapp.datavisualization.algorithm.TranscriptionCluster;
import com.myapp.datavisualization.models.Para;
import com.myapp.datavisualization.services.ParameterService;
import net.seninp.jmotif.sax.SAXException;
import org.apache.spark.api.java.JavaPairRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        @RequestMapping(value = "/algorithm/parameters/get-patterns", method = RequestMethod.GET)
        @CrossOrigin
        public List<String> PatternCal(@RequestParam(value = "MinSupport", required = true) float MinSupport, @RequestParam(value = "MaxPatternLength", required = true) int MaxPatternLength)
        {
            //保存返回值的变量，具体以后什么类型，什么名字，什么方式返回之后整理算法的时候再进行调整。
            List<String> PatternResult;
            Para para=new Para();

//          PatternDiscovery patternDiscovery=new PatternDiscovery(Win_Size,Paa_Size,Alphabet_Size,Norm_Threshold);
            PrefixSpanPattern prefixSpanPattern=new PrefixSpanPattern(MinSupport,MaxPatternLength);
            PatternResult = prefixSpanPattern.getPattern();


            System.out.println(MinSupport);
            System.out.println(MaxPatternLength);


            return PatternResult;


        }
        @RequestMapping(value = "/algorithm/parameters/get-clusters-patterns", method = RequestMethod.GET)
        @CrossOrigin
        public String ClusterCal(@RequestParam(value = "ClusterMinSupport", required = true)  float ClusterMinSupport,@RequestParam(value = "ClusterMaxPatternLength", required = true) int ClusterMaxPatternLength,@RequestParam(value = "K", required = true) int K, @RequestParam(value = "MaxIterations", required = true) int MaxIterations, @RequestParam(value = "Runs", required = true) int Runs)
        {
            //保存返回值的变量，具体以后什么类型，什么名字，什么方式返回之后整理算法的时候再进行调整。
//            JavaPairRDD<Integer, String>  ClusterResult;
//            TranscriptionCluster transcriptionCluster=new TranscriptionCluster(K,MaxIterations,Runs);
//            transcriptionCluster.getCluster();
            System.out.println(ClusterMinSupport);
            System.out.println(ClusterMaxPatternLength);
            System.out.println(K);
            System.out.println(MaxIterations);
            System.out.println(Runs);

            System.out.println("聚类成功");
//            return ClusterResult;
            return "juleichenggong";

        }


}
