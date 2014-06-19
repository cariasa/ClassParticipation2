package edu.unitec.app;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteFileManager {

    public List<Student> readFromFile(Context context,String sourceFileName){
        List<Student> StudentList = new ArrayList<Student>();
        FileReader fr = null;
        BufferedReader br = null;
        File file = null;
        String splitBy = ",";
        try {
            file = new File (sourceFileName);
            fr = new FileReader (file);
            br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() )!= null ){
                String [] std = line.split(splitBy);
                StudentList.add(new Student(Integer.parseInt(std[0].trim()),std[1].trim(),std[2].trim()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if( null != fr ) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return StudentList;
    }

    public String exportStudentGrades( List<String> write_to_file){
        if(MemoriaExternaEscribible()) {
            File file = new File(Environment.getExternalStorageDirectory(), "Grades_ClassParticipation.txt");
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                for(String s: write_to_file){
                    bw.write(s);
                }
                bw.flush();
                bw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        }
        return "";
    }


    public boolean MemoriaExternaEscribible() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean MemoriaExternaLeible() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
