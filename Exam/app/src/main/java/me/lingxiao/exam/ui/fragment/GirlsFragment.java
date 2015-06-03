package me.lingxiao.exam.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lingxiao.exam.R;
import me.lingxiao.exam.model.GirlsItem;
import me.lingxiao.exam.ui.activity.GirlsDetailsActivity;
import me.lingxiao.exam.ui.adapter.GirlsListAdapter;
import me.lingxiao.exam.util.API;
import me.lingxiao.exam.util.HttpCallbackListener;
import me.lingxiao.exam.util.HttpUtils;
import me.lingxiao.exam.util.LogUtils;


public class GirlsFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, View.OnClickListener {

    private boolean scrollFlag = false;
    private int lastVisibleItemPosition;
    private ImageButton refresh;
    private ImageView shadow;
    private ListView mListView;
    private GirlsListAdapter adapter;
    private List<GirlsItem> items = new ArrayList<GirlsItem>();
    private String TAG = "GirlsFragment---->";
    private Object messageHttp;
    private int numbersInOnePage = 0;
    private int page = 1;

    public static final int SHOW_RESPONSE = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    response(msg.obj, 0);
                    break;
                case 1:
                    if (msg.arg1 < items.size()) {
                        items.get(msg.arg1).setPic((Bitmap) msg.obj);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girls, container, false);
        httpRequset(page);
        init(view);
        return view;
    }

    private void init(View v) {
        mListView = (ListView) v.findViewById(R.id.lv_girls);
        adapter = new GirlsListAdapter(getActivity(), R.layout.item_girls_list, items);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);
        refresh = (ImageButton) v.findViewById(R.id.btn_float);
        refresh.setOnClickListener(this);
        shadow = (ImageView) v.findViewById(R.id.btn_float_shadow);
    }

    private void response(Object msg, int startPosition) {
        try {
            JSONObject jsonObject = new JSONObject((String) msg);
            final int currentPage = jsonObject.getInt("current_page");
            JSONArray comments = jsonObject.getJSONArray("comments");
            numbersInOnePage = comments.length();
            LogUtils.d(TAG, startPosition + "");
            LogUtils.d(TAG, numbersInOnePage + "");
            for (int i = startPosition; i < comments.length() && i < startPosition + 10; i++) {
                JSONObject json = (JSONObject) comments.opt(i);
                String title = json.getString("comment_author");
                String date = json.getString("comment_date");
                String picUrl = null;
                JSONArray pics = json.getJSONArray("pics");
                //TODO 这里有问题，如果一个帖子有多个图片，我只能显示一张
                for (int j = 0; j < 1; j++) {
                    picUrl = (String) pics.opt(j);
                    final int position = i + ((currentPage - 1) * numbersInOnePage);
                    HttpUtils.sendHttpRequestFotPic(picUrl, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                        }

                        @Override
                        public void onFinish(Bitmap bitmap) {
                            LogUtils.d(TAG, "得到了bitmap");
                            Message message = new Message();
                            message.what = 1;
                            message.obj = bitmap;
                            message.arg1 = position;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
                }
                int oo = json.getInt("vote_positive");
                int xx = json.getInt("vote_negative");
                int reply = json.getInt("comment_reply_ID");
                int id = json.getInt("comment_ID");
                items.add(new GirlsItem(title, date, picUrl, oo, xx, reply, id));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            scrollFlag = true;
        } else {
            scrollFlag = false;
        }
    }

    //每次拉到底部加载十张
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (messageHttp != null) {
                if (totalItemCount != (page - 1) * numbersInOnePage) {
                    LogUtils.d(TAG, "最下面：" + totalItemCount);
                    if (totalItemCount < numbersInOnePage * page) {
                        response(messageHttp, totalItemCount - (page - 1) * numbersInOnePage);
                    } else {
                        LogUtils.d(TAG, "你不执行么：" + page);
                        httpRequset(++page);
                    }
                }
            }
        }
        if (scrollFlag) {
            if (firstVisibleItem > lastVisibleItemPosition) {
                //上滑
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) refresh.getLayoutParams();
                int fabBottomMargin = lp.bottomMargin;
                refresh.animate().translationY(refresh.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
                shadow.animate().translationY(refresh.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
            if (firstVisibleItem < lastVisibleItemPosition) {
                //下滑
                refresh.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                shadow.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
            if (firstVisibleItem == lastVisibleItemPosition) {
                return;
            }
            lastVisibleItemPosition = firstVisibleItem;
        }
    }

    private void httpRequset(int page) {
        LogUtils.d(TAG, "我看看页数对不对" + page);
        HttpUtils.sendHttpRequest(API.girlsAPI + page, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                LogUtils.d("TAG", response);
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = response;
                messageHttp = response;
                handler.sendMessage(message);
            }

            @Override
            public void onFinish(Bitmap bitmap) {
            }

            @Override
            public void onError(Exception e) {
                LogUtils.d(TAG, e.toString());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GirlsDetailsActivity.actionStart(getActivity(), items.get(position).getPicUrl(),
                items.get(position).getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_float:
                RotateAnimation animation = new RotateAnimation(0f, 1800f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                refresh.startAnimation(animation);

                items.clear();
                numbersInOnePage = 0;
                page = 1;
                httpRequset(page);
                mListView.setSelection(0);
                break;
        }
    }
}
