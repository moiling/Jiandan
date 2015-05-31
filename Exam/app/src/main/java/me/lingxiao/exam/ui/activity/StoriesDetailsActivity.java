package me.lingxiao.exam.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import me.lingxiao.exam.R;
import me.lingxiao.exam.model.StoriesItem;
import me.lingxiao.exam.util.MIUIV6;

public class StoriesDetailsActivity extends MyActivity {

    public static void actionStart(Context context, StoriesItem item) {
        Intent intent = new Intent(context, StoriesDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    private StoriesItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_details);
        item = (StoriesItem) getIntent().getSerializableExtra("item");
        init();
        MIUIV6.setStatusBarTextColor(this, 1);
    }

    private void init() {
        TextView contentView = (TextView) findViewById(R.id.tv_content);
        TextView authorView = (TextView) findViewById(R.id.tv_author);
        TextView authorEmailView = (TextView) findViewById(R.id.tv_author_email);
        TextView dateView = (TextView) findViewById(R.id.tv_date);
        TextView ooView = (TextView) findViewById(R.id.tv_vote_positive);
        TextView xxView = (TextView) findViewById(R.id.tv_vote_negative);
        TextView replyView = (TextView) findViewById(R.id.tv_reply);
        contentView.setText(item.getContent());
        authorView.setText(getResources().getString(R.string.author) + "：" + item.getTitle());
        authorEmailView.setText(getResources().getString(R.string.email) + "：" + item.getEmail());
        dateView.setText(getResources().getString(R.string.date) + "：" + item.getDate());
        ooView.setText("OO：" + item.getVotePositive());
        xxView.setText("XX：" + item.getVoteNegative());
        replyView.setText(getResources().getString(R.string.reply) + "：" + item.getReply());
    }

}
