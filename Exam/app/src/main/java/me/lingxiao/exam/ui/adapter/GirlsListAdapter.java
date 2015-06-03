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
import me.lingxiao.exam.util.LogUtils;

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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resouceId, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.votePositive = (TextView) convertView.findViewById(R.id.tv_vote_positive);
            viewHolder.voteNegative = (TextView) convertView.findViewById(R.id.tv_vote_negative);
            viewHolder.reply = (TextView) convertView.findViewById(R.id.tv_reply);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(title);
        viewHolder.date.setText(date);
        viewHolder.votePositive.setText("OO  " + votePositive);
        viewHolder.voteNegative.setText("XX  " + voteNegative);
        viewHolder.reply.setText(convertView.getResources().getString(R.string.reply) + "  " + reply);
        if (bitmap != null) {
            double width = getScreenWidth(mContext);
            double height = getScreenHeight(mContext);
            LogUtils.d("pic---->", "ScreenWidth:" + width);
            LogUtils.d("pic---->", "ScreenHeight:" + width);
            LogUtils.d("pic---->", "BitmapWidth:" + bitmap.getWidth());
            LogUtils.d("pic---->", "BitmapHeight:" + bitmap.getHeight());
            double scaleYX = (double)bitmap.getHeight() / (double)bitmap.getWidth();
            LogUtils.d("pic---->", "BitmapHeight/BitmapWidth:" + scaleYX);
            ViewGroup.LayoutParams lp = viewHolder.pic.getLayoutParams();
            double viewHight = width * scaleYX;
            LogUtils.d("pic---->", "viewHeight:" + viewHight);
            lp.width = (int) width;
            LogUtils.d("pic---->", "LayoutWidth:" + lp.width);
            lp.height = (int) (viewHight);
            LogUtils.d("pic---->", "LayoutHeight:" + lp.height);
            viewHolder.pic.setLayoutParams(lp);
        }
        viewHolder.pic.setImageBitmap(bitmap);
        return convertView;
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
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
