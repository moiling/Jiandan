package me.lingxiao.exam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.lingxiao.exam.R;
import me.lingxiao.exam.model.StoriesItem;

public class StoriesListAdapter extends ArrayAdapter<StoriesItem> {

    private int resouceId;


    public StoriesListAdapter(Context context, int resource, List<StoriesItem> objects) {
        super(context, resource, objects);
        resouceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoriesItem item = getItem(position);
        String title = item.getTitle();
        String date = item.getDate();
        String content = item.getContent();
        int votePositive = item.getVotePositive();
        int voteNegative = item.getVoteNegative();
        int reply = item.getReply();
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resouceId, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.votePositive = (TextView) convertView.findViewById(R.id.tv_vote_positive);
            viewHolder.voteNegative = (TextView) convertView.findViewById(R.id.tv_vote_negative);
            viewHolder.reply = (TextView) convertView.findViewById(R.id.tv_reply);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(title);
        viewHolder.date.setText(date);
        viewHolder.votePositive.setText("OO  " + votePositive);
        viewHolder.voteNegative.setText("XX  " + voteNegative);
        viewHolder.reply.setText(convertView.getResources().getString(R.string.reply) + "  " + reply);
        viewHolder.content.setText(content);
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView date;
        TextView votePositive;
        TextView voteNegative;
        TextView reply;
        TextView content;
    }
}
