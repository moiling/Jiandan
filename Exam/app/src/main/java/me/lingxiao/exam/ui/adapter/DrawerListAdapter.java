package me.lingxiao.exam.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.lingxiao.exam.R;
import me.lingxiao.exam.model.DrawerListItem;

public class DrawerListAdapter extends ArrayAdapter<DrawerListItem> {

    private int resouceId;


    public DrawerListAdapter(Context context, int resource, List<DrawerListItem> objects) {
        super(context, resource, objects);
        resouceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerListItem item = getItem(position);
        String text = item.getText();
        int imageId = item.getImageId();
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resouceId, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(text);
        viewHolder.imageView.setImageResource(imageId);
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}