package edu.ut.softlab.rate;

import java.io.*;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alex on 16-4-12.
 */
public class Main {

    public static void main(String[] args){
//        File file = new File("src/code.txt");
//        InputStreamReader reader = null;
//        try{
//            reader = new InputStreamReader(new FileInputStream(file));
//            BufferedReader br = new BufferedReader(reader);
//            String line = br.readLine();
//
//            File writeName = new File("src/a");
//            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
//
//            while(line != null){
//                String code = line.substring(3,6);
//                out.write(code+" = "+code);
//                out.newLine();
//                out.flush();
//                line = br.readLine();
//                System.out.println(code);
//            }
//            out.close();
//
//        }catch(Exception ex){
//            System.out.println(ex.toString());
//        }

        java.util.Currency.getInstance("USD").getDisplayName(Locale.forLanguageTag("zh"));

    }
}
