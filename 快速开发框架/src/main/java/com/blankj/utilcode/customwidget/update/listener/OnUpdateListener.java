package com.blankj.utilcode.customwidget.update.listener;


import com.blankj.utilcode.customwidget.update.pojo.UpdateInfo;

/**
 * Created by Shelwee on 14-5-16.
 */
public interface OnUpdateListener {
    /**
     * on start check
     */
    public void onStartCheck();

    /**
     * on finish check
     */
    public void onFinishCheck(UpdateInfo info);

    public void onStartDownload();
    
    public void onDownloading(int progress);
    
    public void onFinshDownload();

}
