package com.bryanrady.optimization.handler;

import android.os.AsyncTask;
import android.util.Log;

public class DataModel {

    private OnDataChangeListener mListener;

    /**
     * 同步数据
     *
     * 有问题：
     *      (1)如果多次调用这个方法，就是多次提交了任务，线程池的原因可能会被拒绝。
     *
     *      (2)每调用一次，就会走一次任务，然后刷新一次UI，虽然用户有时候手贱，但这样显然是不行的
     *
     *      可以通过IdleHandler来解决
     *
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
            Log.e("wangqingbin","开始同步数据...");
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
            Log.e("wangqingbin","同步数据成功...");
            if (mListener != null){
                mListener.onDataChange();
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
