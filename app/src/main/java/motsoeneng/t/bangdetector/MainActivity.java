package motsoeneng.t.bangdetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import motsoeneng.t.bangdetector.bang_detector.BangDetector;
import motsoeneng.t.bangdetector.bang_detector.BangDetectorException;

public class MainActivity extends AppCompatActivity {
    private Button btnStart, btnStop;
//    private TextView txtOutput;
    private BangDetector bangDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
//        txtOutput = findViewById(R.id.txtOutput);
//        txtOutput.setEnabled(false);
//        txtOutput.setText("-Init controls done");

        btnStart.setOnClickListener(new BtnStartClick());
        btnStop.setOnClickListener((new BtnStopClick()));
//        printOutput("Listeners set");
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
        String filePath = "";
//        Date executionTime = new Date();
//        filePath = bangDetector.STORAGE_DIRECTORY + "audio_" + executionTime.toString() + ".mp4";
//        printOutput("File Path: " + filePath);
        bangDetector = new BangDetector(filePath, this.getApplicationContext());
        try {
            bangDetector.start();
//            printOutput("Bang Detector Started ...");
        } catch (BangDetectorException ex) {
//            printOutput(ex.getMessage());
        }
    }

    private void recordAudioStop() {
        if (bangDetector != null) {
            bangDetector.stop();
        }
//        printOutput("Recording stopped");
    }

//    private void printOutput(String text) {
//        txtOutput.setText(txtOutput.getText() + "\n->" + text);
//    }

}
