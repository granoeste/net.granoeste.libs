/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.commons.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;

import net.granoeste.commons.util.LogUtils;

public class ImageFabricater {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(ImageFabricater.class);

    // 透過(Alpha)設定
    public static Bitmap transmissive(Bitmap bitmap) {
        Bitmap mutableBitmap = bitmap.copy(Config.ARGB_8888, true);
        int width = mutableBitmap.getWidth();
        int height = mutableBitmap.getHeight();
        int pixels[] = new int[width * height];
        int alphaPixel = mutableBitmap.getPixel(0, 0);

        mutableBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[x + y * width];
                int alpha = pixel;

                if ((pixel & 0x00ffffff) == (alphaPixel & 0x00ffffff)) {
                    alpha = 0x00;
                }
                pixels[x + y * width] = Color.argb(
                        Color.alpha(alpha),
                        Color.red(pixel),
                        Color.green(pixel),
                        Color.blue(pixel));
            }
        }
        mutableBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return mutableBitmap;
    }

    // サイズ変更
    public static Bitmap rescale(Bitmap srcBitmap, int dstWidth, int dstHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        // calculate the scale - in this case
        float scaleWidth = ((float) dstWidth) / srcWidth;
        float scaleHeight = ((float) dstHeight) / srcHeight;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth, srcHeight, matrix,
                true);

        return resizedBitmap;
    }

    /*
        public static Bitmap resize(Bitmap srcBitmap, float dstWidth, float dstHeight) {
            int srcWidth  = srcBitmap.getWidth();
            int srcHeight = srcBitmap.getHeight();

            // createa matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(dstWidth, dstHeight);

            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth, srcHeight, matrix, true);

            return resizedBitmap;
        }
    */
    // モザイク
    public static Bitmap mosaic(Bitmap bitmap, int dot) {
        Bitmap mutableBitmap = bitmap.copy(Config.ARGB_8888, true);

        int orgWidth = mutableBitmap.getWidth();
        int orgHeight = mutableBitmap.getHeight();
        int width = orgWidth / dot;
        int height = orgHeight / dot;
        int square = dot * dot;

        int[] pixels = new int[orgWidth * orgHeight];

        mutableBitmap.getPixels(pixels, 0, orgWidth, 0, 0, orgWidth, orgHeight);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int aa = 0;
                int rr = 0;
                int gg = 0;
                int bb = 0;

                int moveX = i * dot;
                int moveY = j * dot;

                for (int k = 0; k < dot; k++) {
                    for (int l = 0; l < dot; l++) {
                        int dotColor = pixels[moveX + k + (moveY + l) * orgWidth];
                        aa += Color.alpha(dotColor);
                        rr += Color.red(dotColor);
                        gg += Color.green(dotColor);
                        bb += Color.blue(dotColor);
                    }
                }
                aa = aa / square;
                rr = rr / square;
                gg = gg / square;
                bb = bb / square;

                for (int k = 0; k < dot; k++) {
                    for (int l = 0; l < dot; l++) {
                        pixels[moveX + k + (moveY + l) * orgWidth] = Color.argb(aa, rr, gg, bb);
                    }
                }
            }
        }

        mutableBitmap.setPixels(pixels, 0, orgWidth, 0, 0, orgWidth, orgHeight);

        return mutableBitmap;
    }

    // セピア
    // http://stackoverflow.com/questions/4141150/convert-bitmap-to-sepia-in-android
    public static Bitmap toSephia(Bitmap bmpOriginal) {
        int width, height, r, g, b, c, gry;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        int depth = 20;

        Bitmap bmpSephia = Bitmap.createBitmap(width, height, Config.ARGB_4444);
        Canvas canvas = new Canvas(bmpSephia);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setScale(.3f, .3f, .3f, 1.0f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                c = bmpOriginal.getPixel(x, y);

                r = Color.red(c);
                g = Color.green(c);
                b = Color.blue(c);

                gry = (r + g + b) / 3;
                r = g = b = gry;

                r = r + (depth * 2);
                g = g + depth;

                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                bmpSephia.setPixel(x, y, Color.rgb(r, g, b));
            }
        }
        return bmpSephia;
    }

    public static Bitmap toSephia2(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpSephia = Bitmap.createBitmap(width, height, Config.ARGB_4444);

        Canvas canvas = new Canvas(bmpSephia);
        Paint paint = new Paint();
        float[] src = {
                .39f, .35f, .27f, 0f, 0f,
                .77f, .69f, .53f, 0f, 0f,
                .19f, .17f, .13f, 0f, 0f,
                .00f, .00f, .00f, 1f, 0f
        };
        ColorMatrix cm = new ColorMatrix(src);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpSephia;
    }

    // グレイスケール
    // http://stackoverflow.com/questions/3373860/convert-a-bitmap-to-grayscale-in-android
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_4444);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    // ビットマップのフィルタ
    // http://www.saturn.dti.ne.jp/~npaka/android/util/index.html
    @SuppressWarnings("deprecation")
    public static Bitmap filterBitmap(Bitmap bmp, int color, PorterDuff.Mode mode) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, Config.ARGB_4444);
        Canvas c = new Canvas(result);
        BitmapDrawable bd = new BitmapDrawable(bmp);
        bd.setBounds(0, 0, w, h);
        bd.setColorFilter(color, mode);
        bd.draw(c);
        return result;
    }
}
