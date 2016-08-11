package edu.ut.softlab.rate;

import com.notnoop.apns.*;
import edu.ut.softlab.rate.model.Subscribe;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by alex on 16-4-12.
 */
public class Main {

    public static void main(String[] args) {
        ApnsService service =
                APNS.newService()
                        .withCert("aps_development.p12", "8eu3d7wn32")
                        .withSandboxDestination()
                        .build();

        String payload = APNS.newPayload().alertBody("sdfds").build();
        service.push("65abf562bee4866a298754b5265a906df40bedaf6b2b6fdec220f3e9749a4011", payload);
    }
}

