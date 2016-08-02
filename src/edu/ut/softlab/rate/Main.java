package edu.ut.softlab.rate;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by alex on 16-4-12.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(Utility.getUtility().svgToString("web/static/img/flags/ad.svg"));





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


//        Currency currency = java.util.Currency.getInstance("USD");
//
//
//
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("http://country.io/names.json");
//        Map<String, Set<String>> result = new HashMap<>();
//        try{
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream is = entity.getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while((line = reader.readLine()) != null){
//                sb.append(line);
//            }
//
//
//
//            File writeName = new File("src/a");
//            BufferedWriter out = new BufferedWriter(new FileWriter(writeName));
//
//
//
//
//
//
//            JSONObject jsonObject = new JSONObject(sb.toString());
//            for(String key : jsonObject.keySet()){
//                if(result.containsKey(key)){
//                    result.get(key).add(jsonObject.getString(key));
//                }else {
//                    Set<String> set = new HashSet<>();
//                    set.add(jsonObject.getString(key));
//                    result.put(key, set);
//                }
//                System.out.println(jsonObject.getString(key));
//            }
//            for(String key : result.keySet()){
//                StringBuilder sB = new StringBuilder();
//                sB.append(key+" =");
//                for(String country:result.get(key)){
//                    sB.append(" "+country);
//                }
//                out.write(sB.toString());
//                out.newLine();
//                out.flush();
//            }
//            out.close();
//
//            EntityUtils.consume(entity);
//            response.close();
//        }catch (Exception ex){
//            System.out.println(ex.toString());
//        }
//
    }
}
