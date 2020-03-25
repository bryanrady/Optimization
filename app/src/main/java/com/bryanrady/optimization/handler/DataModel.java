package com.bryanrady.optimization.handler;

import android.os.AsyncTask;
import android.util.Log;

public class DataModel {

    private OnDataChangeListener mListener;

    /**
     * 同步数据
     */
    public void syncData(){
        if (mListener != null){
            SyncDataTask dataTask = new SyncDataTask(mListener);
            dataTask.execute();
        }
    }

    static class SyncDataTask extends AsyncTask<Void,Void,Void>{

        private OnDataChangeListener mListener;

        public SyncDataTask(OnDataChangeListener listener){
            this.mListener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("wangqingbin","开始同步数据...");
            //模拟耗时操作
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mListener != null){
                mListener.onDataChange();
                Log.d("wangqingbin","同步数据成功...");
            }
        }
    }

    public void setOnDataChangeListener(OnDataChangeListener listener){
        this.mListener = listener;
    }

    public interface OnDataChangeListener{
        void onDataChange();
    }

}
