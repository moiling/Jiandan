package me.lingxiao.exam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.lingxiao.exam.R;


public class RightDrawerListAdapter extends ArrayAdapter<String> {

    private int resouceId;

    public RightDrawerListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resouceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resouceId, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.tv_item);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(item);
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }
}
