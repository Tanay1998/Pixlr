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
        pixelList.add(new Pixel(R.drawable.brite, "Brite"));
        pixelList.add(new Pixel(R.drawable.newdisco, "Disco"));
        pixelList.add(new Pixel(R.drawable.pacman, "Emoji"));
        pixelList.add(new Pixel(R.drawable.green, "Green"));
        pixelList.add(new Pixel(R.drawable.hell, "Hell"));
        pixelList.add(new Pixel(R.drawable.holiday, "Holiday"));
        pixelList.add(new Pixel(R.drawable.ice, "Ice"));
        pixelList.add(new Pixel(R.drawable.patriot, "Patriot"));
        pixelList.add(new Pixel(R.drawable.sephia, "Sephia"));
        pixelList.add(new Pixel(R.drawable.sunlite, "Sunlite"));
        pixelList.add(new Pixel(R.drawable.vogue, "Vogue"));

        return pixelList;
    }
}
