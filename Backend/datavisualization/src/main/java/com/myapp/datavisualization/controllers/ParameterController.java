package com.myapp.datavisualization.controllers;

import com.myapp.datavisualization.algorithm.PatternProcess;
import com.myapp.datavisualization.algorithm.PrefixSpanPattern;
import com.myapp.datavisualization.algorithm.TranscriptionCluster;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@CrossOrigin
//@RequestMapping(value="/algorithm/parameters")
//@CrossOrigin(origins = "http://xx-domain.com", maxAge = 3600)
public class ParameterController {

//        @RequestMapping(value = "/algorithm/parameters/PrefixSpanGetPatterns", method = RequestMethod.GET)
//        @CrossOrigin
//        public String PatternCal(@RequestParam(value = "MinSupport", required = true) float MinSupport, @RequestParam(value = "MaxPatternLength", required = true) float MaxPatternLength, @RequestParam(value = "Length", required = true) String Length, @RequestParam(value = "NumberOfPatterns", required = true) String NumberOfPatterns)
//        {
//            List<String> PatternResult;
//            String s="";
//            List<String> lenPattern = new ArrayList<String>();
//            int length=0;
//            int numberOfPatterns=0;
//            int i=0;
//
//            PrefixSpanPattern prefixSpanPattern=new PrefixSpanPattern(MinSupport,MaxPatternLength);
//            PatternResult = prefixSpanPattern.getPattern();
//
//            if(Length!="all"){
//                length = Integer.parseInt(Length);
//            }
//            if(NumberOfPatterns!="all"){
//                numberOfPatterns = Integer.parseInt(NumberOfPatterns);
//            }
//
//             //get specific length of patterns
//            for(String pattern : PatternResult)    {
////                System.out.println(pattern);
//                String str[] = pattern.split(",");
//                int l=str.length;
//                if(l==length+1){
//                    lenPattern.add(pattern);
//
//              // s+=pattern;
//                }
//
//            }
//
//            //preprocess for later sort
//            int lenOfSetPattern=lenPattern.size();
//            final String[][] strPattern=new String[lenOfSetPattern][length+1];
//            for(String lenP:lenPattern){
//                String strlen[] = lenP.split(",");
//                for(int j=0;j<length+1;j++){
//                            strPattern[i][j]=strlen[j];
//
//                       }
//                       i++;
//            }
//
//
//            //sort by frequency
//            final int len=length;
//            Arrays.sort(strPattern, new Comparator<String[]>() {
//                @Override
//                public int compare(final String[] entry1, final String[] entry2) {
//                    final String time1 = entry1[len];
//                    final String time2 = entry2[len];
//                    return time2.compareTo(time1);
//                }
//            });
//
//            //get specific number of pattern
//            for (int m=0;m<numberOfPatterns;m++){
//
//               String numPattern=StringUtils.join(strPattern[m], ",");
////               System.out.println(numPattern);
//               s+=numPattern;
//            }
//
//            return s;
//        }

        @RequestMapping(value = "/algorithm/parameters/PrefixSpanGetPatterns", method = RequestMethod.GET)
        @CrossOrigin
        public String ClusterCal(@RequestParam(value = "MinSupport", required = true) float MinSupport, @RequestParam(value = "MaxPatternLength", required = true) float MaxPatternLength, @RequestParam(value = "Length", required = true) String Length, @RequestParam(value = "NumberOfPatterns", required = true) String NumberOfPatterns,@RequestParam(value = "K", required = true) String K, @RequestParam(value = "MaxIterations", required = true) String MaxIterations, @RequestParam(value = "Runs", required = true) String Runs) throws IOException {


            int length=0;
            int numberOfPatterns=0;
            int k=0;
            int runs=0;
            int maxIterations=0;
            String s="";

            if(Length!="all"){
                length = Integer.parseInt(Length);
            }

            if(NumberOfPatterns!="all"){
                numberOfPatterns = Integer.parseInt(NumberOfPatterns);
            }

            k=Integer.parseInt(K);
            maxIterations=Integer.parseInt(MaxIterations);
            runs=Integer.parseInt(Runs);

            if(k==1){
                List<String> PatternResult;
                PatternProcess patternProcess = new PatternProcess(length, numberOfPatterns);
                PrefixSpanPattern prefixSpanPattern=new PrefixSpanPattern(MinSupport,MaxPatternLength);
                PatternResult = prefixSpanPattern.getPattern();
                String FormalPatternResult = patternProcess.getNumberOfPatterns(PatternResult);
                return FormalPatternResult;

            }
            else {
                List<String> patternList = new ArrayList<>();
                TranscriptionCluster transcriptionCluster = new TranscriptionCluster(k, maxIterations, runs);
                List<List<String>> patternString = transcriptionCluster.getCluster();
                int numCluster = 0;
                PatternProcess patternProcess = new PatternProcess(length, numberOfPatterns);
                for (List<String> subPatternString : patternString) {
                    PrefixSpanPattern prefixSpanPattern = new PrefixSpanPattern(MinSupport, MaxPatternLength);
                    List<String> PatternResult = prefixSpanPattern.getPatternForCluster(subPatternString);
                    String FormalPatternResult = patternProcess.getNumberOfPatterns(PatternResult);
                    numCluster++;
                    s+="Cluster"+numCluster+"\n";
                    s+=FormalPatternResult;
//                    patternList.add("Cluster" + numCluster + "\n");
//                    patternList.add(FormalPatternResult);
                }

                return s;
            }


        }

    @RequestMapping(value = "/image/get")
    public static void getPhoto(HttpServletResponse response) throws Exception {

        File file = new File("/Users/Jia/Downloads/cluster.png");
        FileInputStream fis;
        fis = new FileInputStream(file);

        long size = file.length();
        byte[] temp = new byte[(int) size];
        fis.read(temp, 0, (int) size);
        fis.close();
        byte[] data = temp;
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();

    }



}
