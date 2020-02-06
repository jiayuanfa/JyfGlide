package com.example.jyfglide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 继承线程，来下载图片队列
 * 可以看做是饭店的一张桌子来理解
 */
public class BitmapDispatcher extends Thread {

    private LinkedBlockingDeque<BitmapRequest> requestQueue;

    private Handler handler = new Handler(Looper.getMainLooper());  // 线程切换

    /**
     * 所有的ImageView都是去队列中拿出请求去加载图片，所以要传入队列
     * @param requestQueue
     */
    public BitmapDispatcher(LinkedBlockingDeque<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        super.run();
        // 先去队列中获取请求
        while (!isInterrupted()) {
            try {
                BitmapRequest br = requestQueue.take();
                // 设置占位图片
                showLoadingImage(br);
                // 去网络加载图片
                Bitmap bitmap = findBitmapFromNetwork(br);
                // 将图片设置到ImageView上面
                showImageView(br, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置图片到ImageView上面
     * @param br
     * @param bitmap
     */
    private void showImageView(final BitmapRequest br, final Bitmap bitmap) {
        if (bitmap != null && br.getImageView() != null && br.getUrlMd5().equals(br.getImageView().getTag())) {
            final ImageView iv = br.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bitmap);
                    if (br.getRequestListener() != null) {
                        RequestListener requestListener = br.getRequestListener();
                        requestListener.onSuccess(bitmap);
                    }
                }
            });
        }
    }

    /**
     * 加载网络图片
     * @param br
     * @return
     */
    private Bitmap findBitmapFromNetwork(BitmapRequest br) {
        Bitmap bitmap = downlaodImage(br.getUrl());
        return bitmap;
    }

    /**
     * 通过HttpConnection的方式来获取图片
     * @param uri
     * @return
     */
    private Bitmap downlaodImage(String uri) {
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {

            // 加载Bitmap的过程
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 设置占位图片
     * @param br
     */
    private void showLoadingImage(BitmapRequest br) {
        if (br.getResId() > 0 && br.getImageView() != null) {
            final int resId = br.getResId();
            final ImageView imageView = br.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);
                }
            });
        }
    }
}
