package com.myapp.datavisualization.algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderFileLine{
    public static List<String> getFileContent(String path){
        //set the path of input data, this is the data based on groupby session

        List<String> strList=new ArrayList<String>();
        File file=new File(path);
        InputStreamReader read =null;
        BufferedReader reader=null;
        try{
            read =new InputStreamReader(new FileInputStream(file),"utf-8");
            reader =new BufferedReader(read);
            String line;
            while((line=reader.readLine())!=null){

                strList.add(line);
            }
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (read!=null)
                try{
                    read.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
        }
        if(reader!=null){
            try{
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return strList;

    }
}
