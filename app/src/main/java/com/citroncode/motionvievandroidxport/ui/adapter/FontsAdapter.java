package com.citroncode.motionvievandroidxport.ui.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.citroncode.motionvievandroidxport.utils.FontProvider;

import java.util.List;

public class FontsAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;
    private final FontProvider fontProvider;

    public FontsAdapter(Context context, List<String> fontNames, FontProvider fontProvider) {
        super(context, 0, fontNames);
        // save LayoutInflater for later reuse
        this.inflater = LayoutInflater.from(context);
        this.fontProvider = fontProvider;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String fontName = getItem(position);

        vh.textView.setTypeface(fontProvider.getTypeface(fontName));
        vh.textView.setText(fontName);

        return convertView;
    }

    private static class ViewHolder {

        TextView textView;

        ViewHolder(View rootView) {
            textView = (TextView) rootView.findViewById(android.R.id.text1);
        }
    }
}