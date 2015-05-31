package me.lingxiao.exam.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import me.lingxiao.exam.model.CyxbsInfo;


public class MyXMLUtils {
    public static void parseXMLWithPull(String xmlData, MyXMLCallbackListener listener) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String versionName = "";
            String updateContent = "";
            String apkURL = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("versionName".equals(nodeName)) {
                            versionName = xmlPullParser.nextText();
                        } else if ("updateContent".equals(nodeName)) {
                            updateContent = xmlPullParser.nextText();
                        } else if ("apkURL".equals(nodeName)) {
                            apkURL = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("updataInfo".equals(nodeName)) {
                            CyxbsInfo cyxbsInfo = new CyxbsInfo("0", versionName, updateContent, apkURL);
                            if (listener != null) {
                                listener.onFinish(cyxbsInfo);
                            }
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e);
            }
        }

    }

}
