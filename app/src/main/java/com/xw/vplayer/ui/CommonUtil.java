package com.xw.vplayer.ui;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.xw.helper.utils.StringUtil;
import com.xw.vplayer.happyApplication;
import com.xw.helper.utils.MLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CommonUtil {

    private static final String CONTENT_URI_QUERY = "content://com.hunantv.operator.mango.sp.provider/spaction";


    public interface ActionColumns extends BaseColumns {
        String NAME = "name";
        String VALUE = "value";
    }
    /**
     * 用于查询sp跳转能力
     *
     * @return
     */
    public static int getAbility(String pageSource) {
        MLog.d("getAbility in info:" + pageSource);
        Uri uri = Uri.parse(CONTENT_URI_QUERY);
        Cursor mCursor = happyApplication.getInstance().getContentResolver().query(uri, null,
                null, null, null);
        int result = 0;
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                String name = mCursor.getString(mCursor.getColumnIndex(ActionColumns.NAME));
                if (pageSource.equals(name)) {
                    result = mCursor.getInt(mCursor.getColumnIndex(ActionColumns.VALUE));
                    MLog.d( "query:" + result);
                }
            }
            MLog.d("getAbility result:" + result);
            mCursor.close();
        }
        return result;
    }
    /**
     * @description 解析接口下发配置
     * @author Mr.xw
     * @time 2021/1/11 16:22
     */

    public static String addMap2Url(String url, Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (builder.length() == 0) {
                builder.append(url + "?" + key + "=" + value);
            } else {
                builder.append("&" + key + "=" + value);
            }
        }
        return builder.toString();
    }

}
