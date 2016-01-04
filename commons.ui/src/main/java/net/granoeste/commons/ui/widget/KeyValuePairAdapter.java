package net.granoeste.commons.ui.widget;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class KeyValuePairAdapter<F, String> extends ArrayAdapter<Pair<F, String>> {

    /**
     * コンストラクタ
     *
     * @param context    スピナーを利用するアクティビティ
     * @param resourceId スピナー表示レイアウトのリソースID
     * @param list       ドロップダウンリストに設定する配列
     */
    public KeyValuePairAdapter(Context context, int resourceId, ArrayList<Pair<F, String>> list) {
        super(context, resourceId, list);
    }

    /**
     * コンストラクタ
     *
     * @param context    スピナーを利用するアクティビティ
     * @param resourceId スピナー表示レイアウトのリソースID
     */
    public KeyValuePairAdapter(Context context, int resourceId, F[] firstArray, String[] secondArray) {
        this(context, resourceId, new ArrayList<Pair<F, String>>());
        int arrayLength = firstArray.length;
        for (int i = 0; i < arrayLength; i++) {
            super.add(new Pair<F, String>(firstArray[i], secondArray[i]));

        }
    }


    // スピナー表示用
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(pos, convertView, parent);
        textView.setText((CharSequence) getItem(pos).second);
        return textView;
    }

    // スピナードロップダウン表示用
    @Override
    public View getDropDownView(int pos, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(pos,
                convertView,
                parent);
        textView.setText((CharSequence) getItem(pos).second);
        return textView;
    }
}
