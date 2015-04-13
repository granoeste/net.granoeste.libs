/**
 * ColorMatrix
 * <p/>
 * Based on Mario Klingemann's wonderful AS2 ColorMatrix Class
 * <p/>
 * Quasimondo Libs - Tinker tools by Mario Klingemann
 *
 * @author Copyright Mario Klingemann and muta
 * @see http://code.google.com/p/quasimondolibs/
 * @see http://www.libspark.org/svn/as3/ColorMatrix/src/com/quasimondo/geom/ColorMatrix.as
 * @see http://www.quasimondo.com/
 * @see http://www.quasimondo.com/archives/000565.php
 * @see http://www.graficaobscura.com/matrix/
 * @see http://www.graficaobscura.com/matrix/matrix.c
 * @see http://unbland.net/blog/
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.granoeste.commons.image;

import net.granoeste.commons.util.LogUtils;

public class ColorMatrix extends android.graphics.ColorMatrix {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(ColorMatrix.class);

    // 輝度(Luminance)
    private final float LUM_R = 0.213f;
    private final float LUM_G = 0.715f;
    private final float LUM_B = 0.072f;

    public ColorMatrix() {
        super();
    }

    public ColorMatrix(ColorMatrix src) {
        super(src);
    }

    public ColorMatrix(float[] src) {
        super(src);
    }

    /**
     * 色相 (Hue) 彩度 (Saturation) 明度 (Brightness) コントラスト (Contrast)　を一括で変更します。
     *
     * @param hue -180 <- 0 -> 180 の範囲で変更します。
     * @param saturation 0 以上で変更します。
     * @param brightness -1 <- 0 -> 1 の範囲で変更します。
     * @param contrast -1 <- 0 -> 1 の範囲で変更します。
     */
    public void adjustColor(float hue, float saturation, float brightness, float contrast) {
        reset();
        adjustHue(hue);
        adjustSaturation(saturation);
        adjustBrightness(brightness);
        adjustContrast(contrast);
    }

    /**
     * 色相 (Hue) を変更します。
     *
     * @param value -180 <- 0 -> 180 の範囲で指定します。
     */
    public void adjustHue(float value) {
        float lumR = LUM_R;
        float lumG = LUM_G;
        float lumB = LUM_B;

        float radians = (float) (value * Math.PI / 180);

        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float[] src = {
                lumR + cos * (1 - lumR) + sin * -lumR, lumG + cos * -lumG + sin * -lumG,
                lumB + cos * -lumB + sin * (1 - lumB), 0, 0,
                lumR + cos * -lumR + sin * 0.143f, lumG + cos * (1 - lumG) + sin * 0.140f,
                lumB + cos * -lumB + sin * -0.283f, 0, 0,
                lumR + cos * -lumR + sin * -(1 - lumR), lumG + cos * -lumG + sin * lumG,
                lumB + cos * (1 - lumB) + sin * lumB, 0, 0,
                0, 0, 0, 1, 0
        };

        concat(src);
    }

    /**
     * 彩度 (Saturation) を変更します。
     * 0 の時にグレースケールになります。
     *
     * @param value 0 以上を指定します。
     */
    public void adjustSaturation(float value) {
        float n = 1 - value;

        float r = LUM_R * n;
        float g = LUM_G * n;
        float b = LUM_B * n;

        float[] src = {
                r + value, g, b, 0, 0,
                r, g + value, b, 0, 0,
                r, g, b + value, 0, 0,
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * 明度 (Brightness) を変更します。
     *
     * @param value -1 <- 0 -> 1 の範囲で指定します。
     */
    public void adjustBrightness(float value) {
        float[] src = {
                1, 0, 0, value, 0,
                0, 1, 0, value, 0,
                0, 0, 1, value, 0,
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * 明度 (Brightness) を変更します。
     *
     * @param r -1 <- 0 -> 1 の範囲で指定します。
     * @param g -1 <- 0 -> 1 の範囲で指定します。
     * @param b -1 <- 0 -> 1 の範囲で指定します。
     */
    public void adjustBrightness(float r, float g, float b) {
        float[] src = {
                1, 0, 0, r, 0,
                0, 1, 0, g, 0,
                0, 0, 1, b, 0,
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * コントラスト (Contrast) を変更します。
     *
     * @param value -1 <- 0 -> 1 の範囲で指定します。
     */
    public void adjustContrast(float value) {
        float[] src = {
                value + 1, 0, 0, 0, -(128 * value),
                0, value + 1, 0, 0, -(128 * value),
                0, 0, value + 1, 0, -(128 * value),
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * 指定の色で着色します。
     *
     * @param rgb 着色する色です。
     */
    public void colorize(int rgb) {
        colorize(rgb, 1f);
    }

    /**
     * 指定の色で着色します。
     *
     * @param rgb 着色する色です。
     * @param amount 着色する度合いです。
     */
    public void colorize(int rgb, float amount) {
        float lumR = LUM_R;
        float lumG = LUM_G;
        float lumB = LUM_B;

        float n = 1 - amount;

        int r = rgb >> 16 & 0xff / 255;
        int g = rgb >> 8 & 0xff / 255;
        int b = rgb & 0xff / 255;

        float[] src = {
                n + amount * r * lumR, amount * r * lumG, amount * r * lumB, 0, 0,
                amount * g * lumR, n + amount * g * lumG, amount * g * lumB, 0, 0,
                amount * b * lumR, amount * b * lumG, n + amount * b * lumB, 0, 0,
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * 色調をランダムで変更します。
     */
    public void randomize() {
        randomize(1);
    }

    /**
     * 色調をランダムで変更します。
     *
     * @param amount ランダムで変更する度合いです。
     */
    public void randomize(float amount) {
        float n = 1 - amount;
        float[] src = {
                amount * randomMargin() + n, amount * randomMargin(), amount * randomMargin(), 0,
                amount * 255 * randomMargin(),
                amount * randomMargin(), amount * randomMargin() + n, amount * randomMargin(), 0,
                amount * 255 * randomMargin(),
                amount * randomMargin(), amount * randomMargin(), amount * randomMargin() + n, 0,
                amount * 255 * randomMargin(),
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * @private
     */
    private float randomMargin() {
        return (float) (Math.random() - Math.random());
    }

    /**
     * 色調を反転します。
     */
    public void invert() {
        float[] src = {
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        };
        concat(src);
    }

    /**
     * 本来の色調に戻します。
     */
    public void identity() {
        super.reset();
    }

    public void setChannels(int r, int g, int b, int a) {
        float rf = ((r & 1) == 1 ? 1 : 0) + ((r & 2) == 2 ? 1 : 0) + ((r & 4) == 4 ? 1 : 0)
                + ((r & 8) == 8 ? 1 : 0);
        if (rf > 0) {
            rf = 1 / rf;
        }
        float gf = ((g & 1) == 1 ? 1 : 0) + ((g & 2) == 2 ? 1 : 0) + ((g & 4) == 4 ? 1 : 0)
                + ((g & 8) == 8 ? 1 : 0);
        if (gf > 0) {
            gf = 1 / gf;
        }
        float bf = ((b & 1) == 1 ? 1 : 0) + ((b & 2) == 2 ? 1 : 0) + ((b & 4) == 4 ? 1 : 0)
                + ((b & 8) == 8 ? 1 : 0);
        if (bf > 0) {
            bf = 1 / bf;
        }
        float af = ((a & 1) == 1 ? 1 : 0) + ((a & 2) == 2 ? 1 : 0) + ((a & 4) == 4 ? 1 : 0)
                + ((a & 8) == 8 ? 1 : 0);
        if (af > 0) {
            af = 1 / af;
        }

        float[] src = {
                (r & 1) == 1 ? rf : 0, (r & 2) == 2 ? rf : 0, (r & 4) == 4 ? rf : 0,
                (r & 8) == 8 ? rf : 0, 0,
                (g & 1) == 1 ? gf : 0, (g & 2) == 2 ? gf : 0, (g & 4) == 4 ? gf : 0,
                (g & 8) == 8 ? gf : 0, 0,
                (b & 1) == 1 ? bf : 0, (b & 2) == 2 ? bf : 0, (b & 4) == 4 ? bf : 0,
                (b & 8) == 8 ? bf : 0, 0,
                (a & 1) == 1 ? af : 0, (a & 2) == 2 ? af : 0, (a & 4) == 4 ? af : 0,
                (a & 8) == 8 ? af : 0, 0
        };
        concat(src);
    }

    public void blend(ColorMatrix colorMatrix, int amount) {
        float n = 1 - amount;

        float matrix[] = super.getArray();
        float blend[] = colorMatrix.getArray();

        for (int i = 0; i < 20; ++i) {
            matrix[i] = n * matrix[i] + amount * blend[i];
        }
        concat(matrix);
    }

    public void luminance2Alpha() {
        float[] src = {
                0, 0, 0, 0, 255,
                0, 0, 0, 0, 255,
                0, 0, 0, 0, 255,
                LUM_R, LUM_G, LUM_B, 0, 0
        };
        concat(src);
    }

    public void setAlpha(float alpha) {
        float[] src = {
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, alpha, 0
        };
        concat(src);
    }

    /**
     * 行列を現在の行列と連結して、2 つの行列の色調効果を効果的に組み合わせます。
     *
     * @param array 5×4 の 20 個の行列からなる Array オブジェクトです。
     */
    public void concat(float array[]) throws IllegalArgumentException {
        float[] temp = new float[20];
        int i = 0;

        if (array.length != 20) {
            throw new IllegalArgumentException("指定できるのは  5×4 の 20 個の行列からなる Array オブジェクトです。");
        }

        float[] matrix = super.getArray();

        for (int y = 0; y < 4; ++y) {
            for (int x = 0; x < 5; ++x) {
                temp[i + x] =
                        array[i] * matrix[x] +
                                array[i + 1] * matrix[x + 5] +
                                array[i + 2] * matrix[x + 10] +
                                array[i + 3] * matrix[x + 15] +
                                ((x == 4) ? array[i + 4] : 0);
            }
            i += 5;
        }
        super.set(temp);
    }

    /**
     * 新しい ColorMatrix インスタンスとして、この行列のクローンを返します。
     */
    @Override
    public ColorMatrix clone() {
        return new ColorMatrix(super.getArray());
    }

}
