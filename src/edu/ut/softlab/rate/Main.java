package edu.ut.softlab.rate;

import com.notnoop.apns.*;
import edu.ut.softlab.rate.model.Subscribe;


/**
 * Created by alex on 16-4-12.
 */
public class Main {

    public static void main(String[] args) {
//        ApnsService service =
//                APNS.newService()
//                        .withCert("aps_development.p12", "8eu3d7wn32")
//                        .withSandboxDestination()
//                        .build();
//
//        String payload = APNS.newPayload().alertBody("Can't be simpler than this!").build();
//        String token = "0f1d5131 5e7d1569 af26c315 0779fcb3 7d9057f9 a920da9e ae6e8bc1 53abb0d2";
//        service.push(token, payload);
        System.out.println("2016-08-09".compareTo("2016-08-08"));
    }
}

