package com.example.jyfglide;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

/**
 * 图片请求类
 * 这里面只包含图片请求的一些东西，不能包含其他东西
 */
public class BitmapRequest {

    private String url; // 请求的url
    private Context context;    // 上下文
    private int resId;  // 占位图
    private RequestListener requestListener;    // 回调
    private SoftReference<ImageView> imageView; // 出现内存不足的情况的时候，回收，所以使用软引用
    private String urlMd5;  // 防止复用的时候出现的错乱的问题

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    /**
     * 此处要返回一个ImageView对象，而不是一个软引用的ImageView
     * @return
     */
    public ImageView getImageView() {
        return imageView.get();
    }

    public void setImageView(SoftReference<ImageView> imageView) {
        this.imageView = imageView;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }

    public BitmapRequest(Context context) {
        this.context = context;
    }

    /**
     * url
     * @param url
     * @return
     */
    public BitmapRequest load(String url) {
        this.url = url;
        this.urlMd5 = Md5Utils.toMD5(url);
        return this;
    }

    /**
     * 占位图
     * @param resId
     * @return
     */
    public BitmapRequest loading(int resId) {
        this.resId = resId;
        return this;
    }

    /**
     * 回调
     * @param requestListener
     * @return
     */
    public BitmapRequest listener(RequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    /**
     * 最后设置到ImageView
     * @param imageView
     * @return
     */
    public BitmapRequest into(ImageView imageView) {
        imageView.setTag(this.urlMd5);
        this.imageView = new SoftReference<>(imageView);
        // 把自己丢到队列中
        RequestManager.getInstance().addBitmapRequest(this);
        return this;
    }
}
