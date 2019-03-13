package com.myapp.datavisualization.algorithm;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PatternProcess {

    private int Length;
    private int NumberOfPatterns;

    public PatternProcess(int Length, int NumberOfPatterns){
        this.Length=Length;
        this.NumberOfPatterns=NumberOfPatterns;
    }


    public String getNumberOfPatterns(List<String> PatternResult) {

        String s="";
        List<String> lenPattern = new ArrayList<String>();

        int numberOfPatterns=0;


//        if(Length!="all"){
//            length = Integer.parseInt(Length);
//        }
//        if(NumberOfPatterns!="all"){
//            numberOfPatterns = Integer.parseInt(NumberOfPatterns);
//        }

//        //提取最后的数值排序
//        List<String> nextList=new ArrayList<>();
//        for(String sor:PatternResult){
//            String strNext[]=sor.split(",");
//            String next=strNext[strNext.length-1];
//            System.out.println(next+"\n");
//            nextList.add(next);
//        }


        //get specific length of patterns
        for(String pattern : PatternResult){
            String str[] = pattern.split(",");
            int l=str.length;
            if(l==Length+1){
                lenPattern.add(pattern);

                // s+=pattern;
                }
        }


        //preprocess for later sort
        int lenOfSetPattern=lenPattern.size();
        final String[][] strPattern=new String[lenOfSetPattern][Length+1];

        int i=0;
        for(String lenP:lenPattern){
            String strlen[] = lenP.split(",");
            for(int j=0;j<Length+1;j++){
                strPattern[i][j]=strlen[j];
            }
            i++;

        }

        //sort by frequency
        final int len=Length;
        Arrays.sort(strPattern, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final String time1 = entry1[len];
                final String time2 = entry2[len];
                 //return comparing result, space of data should be remove before transfer to int from string
                return new Double((String) time2.trim()).compareTo(new Double((String) time1.trim()));
            }
        });

        //get specific number of pattern
        for (int m=0;m<NumberOfPatterns;m++){

            String numPattern= StringUtils.join(strPattern[m], ",");

            s+=numPattern;
        }

        return s;

    }
}
