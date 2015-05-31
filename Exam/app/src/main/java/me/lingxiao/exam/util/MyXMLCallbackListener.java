package me.lingxiao.exam.util;

import me.lingxiao.exam.model.CyxbsInfo;

public interface MyXMLCallbackListener {
    void onFinish(CyxbsInfo cyxbsInfo);
    void onError(Exception e);
}
