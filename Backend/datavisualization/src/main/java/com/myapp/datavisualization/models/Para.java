package com.myapp.datavisualization.models;


import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Para  implements Serializable{

    private float MinSupport;
    private int MaxPatternLength;

//    public Para(int WIN_SIZE,int PAA_SIZE, int ALPHABET_SIZE, double NORM_THRESHOLD) {
//        this.WIN_SIZE=WIN_SIZE;
//        this.PAA_SIZE=PAA_SIZE;
//        this.ALPHABET_SIZE=ALPHABET_SIZE;
//        this.NORM_THRESHOLD=NORM_THRESHOLD;
//    }

    public void setMinSupport(float MinSupport){
        this.MinSupport=MinSupport;
    }
    public float getMinSupport(){
        return MinSupport;
    }
    public void setMaxPatternLength(int MaxPatternLength){
        this.MaxPatternLength=MaxPatternLength;
    }
    public int  getMaxPatternLength(){
        return MaxPatternLength;
    }

}
