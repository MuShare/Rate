package edu.ut.softlab.rate;

import com.notnoop.apns.*;
import edu.ut.softlab.rate.model.Subscribe;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Created by alex on 16-4-12.
 */
public class Main {

    public static void main(String[] args) {
////        ApnsService service =
////                APNS.newService()
////                        .withCert("aps_development.p12", "8eu3d7wn32")
////                        .withSandboxDestination()
////                        .build();
////
////        String payload = APNS.newPayload().alertBody("sdfds").build();
////        service.push("65abf562bee4866a298754b5265a906df40bedaf6b2b6fdec220f3e9749a4011", payload);
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        Date date = new Date();
//        Calendar cl = Calendar.getInstance();
//        cl.set(2013, Calendar.JANUARY, 1);
//        cl.setTimeInMillis(Utility.getZeroTime(cl.getTime()));
//        Calendar cl2 = Calendar.getInstance();
//        cl2.set(2013, Calendar.JANUARY, 1, 0, 0, 0);
//        System.out.println(cl.getTimeInMillis());
//        System.out.println(cl2.getTimeInMillis());

//        try{
//            File input = new File("src/rate_country_location.properties");
//            File output = new File("src/t.properties");
//            FileReader fileReader = new FileReader(input);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//            FileWriter fileWriter = new FileWriter(output);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            String line;
//            while ((line = bufferedReader.readLine()) != null){
//                String[] data = line.split("\\t");
//                bufferedWriter.write(data[0]+"="+data[1]+","+data[2]);
//                bufferedWriter.newLine();
//            }
//            bufferedReader.close();
//            bufferedWriter.flush();
//            bufferedWriter.close();
//
////            File supplement = new File("src/rate_supplement.properties");
////            FileWriter fileWriter = new FileWriter(supplement, true);
////            BufferedWriter writer = new BufferedWriter(fileWriter);
////            writer.write(code + " = " + code + "\n");
////            writer.flush();
////            writer.close();
//        }catch (Exception ex){
//            System.out.println(ex.toString());
//        }


        String s = "37.09024,-95.712891";
        String[] data = s.split(",");
        System.out.println(data[0]);

    }
}

