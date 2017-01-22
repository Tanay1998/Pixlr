package com.moto.pixelr.data;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class PixelCodes {

    public static byte[] getPixelByteCode(int position) {
        int[] pixelCode = getPixelCode(position);
        return int2byte(pixelCode);
    }

    private static int[] getPixelCode(int position) {

        switch(position) {

            // LIGHTNING:
            case 0:
                return new int[]{
                        0x00000000, 0x00000000, 0x00000000, 0xff11f8ed, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0x00000000, 0xff11f8ed, 0xff11f8ed, 0x00000000, 0x00000000, 0xff11f8ed, 0x00000000,
                        0x00000000, 0xff11f8ed, 0x00000000, 0xff11f8ed, 0x00000000, 0xff11f8ed, 0x00000000, 0x00000000,
                        0xff11f8ed, 0x00000000, 0x00000000, 0xff11f8ed, 0xff11f8ed, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0x00000000, 0x00000000, 0xff11f8ed, 0x00000000, 0x00000000, 0x00000000, 0x00000000
                };

            // DISCO
            case 1:
                return new int[]{
                        0xffffffff, 0xffffffff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffda0517, 0xffda0517, 0xffe912ff,
                        0xffffffff, 0xffffffff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffda0517, 0xffda0517, 0xffe912ff,
                        0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4,
                        0xffffd912, 0xffffd912, 0xffffd912, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xff48da0f,
                        0xffffd912, 0xffffd912, 0xffffd912, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xff48da0f, 0xffe912ff,
                        0xff48da0f, 0xff48da0f, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xffffd912, 0xffffd912, 0xffffd912,
                        0xff48da0f, 0xff48da0f, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xff48da0f, 0xff48da0f, 0xffffffff,
                        0xff12fff4, 0xffffd912, 0xff12fff4, 0xff12fff4, 0xffe912ff, 0xff48da0f, 0xff12fff4, 0xffffffff,
                        0xffe912ff, 0xffda0517, 0xffda0517, 0xff12fff4, 0xffe912ff, 0xff48da0f, 0xff48da0f, 0xffffffff,
                        0xffe912ff, 0xffda0517, 0xffda0517, 0xff12fff4, 0xffe912ff, 0xffffffff, 0xffffffff, 0xffda0517
                };

            // PAC-MAN
            case 2:
                return new int[]{
                        0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000,
                        0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
                        0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000
                };

            // GREEN
            case 3:
                return new int[]{
                        0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13,
                        0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13,
                        0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13,
                        0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13,
                        0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13
                };

            // HELL
            case 4:
                return new int[]{
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc
                };

            // HOLIDAY
            case 5:
                return new int[]{
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a,
                        0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a,
                        0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a
                };

            // ICE
            case 6:
                return new int[]{
                        0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e,
                        0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e,
                        0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e,
                        0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e,
                        0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e
                };

            // PATRIOT
            case 7:
                return new int[] {
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc,
                        0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff,
                        0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300,
                        0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300
                };

            // SEPHIA
            case 8:
                return new int[]{
                        0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5,
                        0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5,
                        0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5,
                        0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5,
                        0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5
                };

            // SUNLITE
            case 9:
                return new int[]{
                        0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff,
                        0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff,
                        0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff,
                        0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff,
                        0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff
                };

            // VOGUE
            case 10:
                return new int[]{
                        0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff,
                        0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff,
                        0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff,
                        0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff,
                        0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff
                };



            // RED & WHITE:
            case 11:
                return new int[]{
                        0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14,
                        0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14,
                        0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14,
                        0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14,
                        0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14
                };

            // DEBUG 0x00:
            case 100:
                return new int[]{0x00};

            // DEBUG 0x01:
            case 101:
                return new int[]{0x01};

            // DEBUG 0x02:
            case 102:
                return new int[]{0x02};

            // DEFAULT:
            default:
                return new int[]{0x00};
        }
    }

    private static byte[] int2byte(int[]src) {
        int srcLength = src.length;
        byte[]dst = new byte[srcLength << 2];

        for (int i=0; i<srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) ((x >>> 0) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }
}
