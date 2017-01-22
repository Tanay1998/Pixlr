package com.moto.pixelr.data;

import com.moto.pixelr.R;
import com.moto.pixelr.model.Pixel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class PixelList {

    public static List<Pixel> generatePixelList() {

        List<Pixel> pixelList = new LinkedList<>();

        // Adds the pixel options here.
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));
        pixelList.add(new Pixel(R.drawable.mm_helmet, "Helmet"));

        return pixelList;
    }
}
