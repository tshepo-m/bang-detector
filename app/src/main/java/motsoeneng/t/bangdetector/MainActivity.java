package motsoeneng.t.bangdetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import motsoeneng.t.bangdetector.bang_detector.BangDetector;
import motsoeneng.t.bangdetector.bang_detector.BangDetectorException;

public class MainActivity extends AppCompatActivity {
    private Button btnStart, btnStop;
    private BangDetector bangDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new BtnStartClick());
        btnStop.setOnClickListener((new BtnStopClick()));
    }

    private class BtnStartClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            recordAudioStart();
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        }
    }

    private class BtnStopClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            recordAudioStop();
            btnStop.setEnabled(false);
            btnStart.setEnabled(true);

        }
    }

    private void recordAudioStart() {
        bangDetector = new BangDetector(this.getApplicationContext());
        try {
            bangDetector.start();
        } catch (BangDetectorException ex) {
            Log.i(this.getClass().getName(), ex.getMessage());
        }
    }

    private void recordAudioStop() {
        if (bangDetector != null) {
            bangDetector.stop();
        }
    }
}
