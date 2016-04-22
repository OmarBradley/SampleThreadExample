package com.tacadem.samplethreadexample;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;

import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.text_message) TextView textView;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.button) Button button;

    Handler handler = new Handler(Looper.getMainLooper());

    Function<Integer, Runnable> progressRunnable = (progress) -> {
        Runnable runnable = () -> {
            textView.setText("progress : " + progress);
            progressBar.setProgress(progress);
        };
        return runnable;
    };

    Runnable completeRunnable = () -> {
        textView.setText("download complete");
        progressBar.setProgress(100);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        button.setOnClickListener(view -> {
            startDownload();
        });
    }

    private void startDownload() {
        new Thread(() -> {
            int progress = 0;
            while (progress <= 100) {
                handler.post(progressRunnable.apply(progress));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                progress += 5;
            }
            handler.post(completeRunnable);
        }).start();
    }
}
