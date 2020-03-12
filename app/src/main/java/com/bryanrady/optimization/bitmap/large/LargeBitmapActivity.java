package com.bryanrady.optimization.bitmap.large;

import android.os.Bundle;

import com.bryanrady.optimization.R;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: wangqingbin
 * @date: 2020/3/12 16:28
 */
public class LargeBitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_large_image);
        LargeImageView largeImageView = findViewById(R.id.largeImageView);
        InputStream is = null;
        try {
            is = getResources().getAssets().open("big.png");
            largeImageView.setImage(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
