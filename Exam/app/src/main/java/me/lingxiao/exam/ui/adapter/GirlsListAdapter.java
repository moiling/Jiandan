package me.lingxiao.exam.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.lingxiao.exam.R;
import me.lingxiao.exam.model.GirlsItem;

public class GirlsListAdapter extends ArrayAdapter<GirlsItem> {

    private int resouceId;
    private Context mContext;


    public GirlsListAdapter(Context context, int resource, List<GirlsItem> objects) {
        super(context, resource, objects);
        resouceId = resource;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GirlsItem item = getItem(position);
        String title = item.getTitle();
        String date = item.getDate();
        int votePositive = item.getVotePositive();
        int voteNegative = item.getVoteNegative();
        int reply = item.getReply();
        Bitmap bitmap = item.getPic();

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resouceId, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.date = (TextView) view.findViewById(R.id.tv_date);
            viewHolder.votePositive = (TextView) view.findViewById(R.id.tv_vote_positive);
            viewHolder.voteNegative = (TextView) view.findViewById(R.id.tv_vote_negative);
            viewHolder.reply = (TextView) view.findViewById(R.id.tv_reply);
            viewHolder.pic = (ImageView) view.findViewById(R.id.iv_pic);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(title);
        viewHolder.date.setText(date);
        viewHolder.votePositive.setText("OO: " + votePositive);
        viewHolder.voteNegative.setText("XX: " + voteNegative);
        viewHolder.reply.setText(view.getResources().getString(R.string.reply) + ": " + reply);
        if (bitmap != null) {
            double width = getScreenWidth(mContext);
            double hight = getScreenHight(mContext);
            double scaleYX = bitmap.getHeight() / bitmap.getWidth();
            ViewGroup.LayoutParams lp = viewHolder.pic.getLayoutParams();
            lp.width = (int) width;
            if ((int) (width * scaleYX) != 0) {
                lp.height = (int) (width * scaleYX);
            } else {
                lp.height = (int) (hight / 3);
            }
            viewHolder.pic.setLayoutParams(lp);
        }
        viewHolder.pic.setImageBitmap(bitmap);
        return view;
    }

    private class ViewHolder {
        TextView title;
        TextView date;
        TextView votePositive;
        TextView voteNegative;
        TextView reply;
        ImageView pic;
    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    public static int getScreenHight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
