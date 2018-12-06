package com.myapp.datavisualization.algorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.myapp.datavisualization.models.Para;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.SAXProcessor;
import net.seninp.jmotif.sax.TSProcessor;
import net.seninp.jmotif.sax.alphabet.Alphabet;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructure.SAXRecord;
import net.seninp.jmotif.sax.datastructure.SAXRecords;

//语法逻辑错误还需要更改
//参数调用怎么整

public class PatternDiscovery {
    private static final String timeSeriesData = "/Users/Jia/Documents/workspaces/motifDiscovery/src/motifDiscovery/oneDimension.csv";
    private static final String CR = "\n";
    private static final String COMMA = ", ";
    private int WIN_SIZE;
    private int PAA_SIZE;
    private int ALPHABET_SIZE;
    private double NORM_THRESHOLD;

    public PatternDiscovery(int WIN_SIZE,int PAA_SIZE,int ALPHABET_SIZE,double NORM_THRESHOLD) {

        this.WIN_SIZE=WIN_SIZE;
        this.PAA_SIZE=PAA_SIZE;
        this.ALPHABET_SIZE=ALPHABET_SIZE;
        this.NORM_THRESHOLD=NORM_THRESHOLD;
    }

    public String GetPattern()throws IOException, SAXException
    {
        // read the data
        double[] series = TSProcessor.readFileColumn(timeSeriesData, 0, 0);

        // instantiate classes
        Alphabet na = new NormalAlphabet();
        SAXProcessor sp = new SAXProcessor();

        // perform discretization
        SAXRecords saxData = sp.ts2saxViaWindow(series,WIN_SIZE,PAA_SIZE, na.getCuts(ALPHABET_SIZE),
                NumerosityReductionStrategy.NONE, NORM_THRESHOLD);



        ArrayList<Integer> indexes = new ArrayList<Integer>();
        indexes.addAll(saxData.getIndexes());
        Collections.sort(indexes);

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/Users/Jia/Documents/workspaces/motifDiscovery/src/motifDiscovery/SaxByExc.csv")));
        for (Integer idx : indexes) {
            bw.write(idx + COMMA + String.valueOf(saxData.getByIndex(idx).getPayload()) + CR);
        }
        bw.close();

        // get the list of 10 most frequent SAX words
        ArrayList<SAXRecord> motifs = saxData.getMotifs(10);
        SAXRecord topMotif = motifs.get(0);

        // print motifs
        System.out.println("top motif " + String.valueOf(topMotif.getPayload()) + " seen " +
                topMotif.getIndexes().size() + " times.");
        return String.valueOf(topMotif.getPayload());

   }

}
