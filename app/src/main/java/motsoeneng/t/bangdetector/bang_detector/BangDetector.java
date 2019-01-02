package motsoeneng.t.bangdetector.bang_detector;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class BangDetector {
    private MediaRecorder recorder;
    private AudioAmplitudeListener amplitudeListener = null;
    private Context context;
    public static final String STORAGE_DIRECTORY = "/sdcard/Audio/";

    public BangDetector(String storagePath, Context context) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        if (storagePath.isEmpty() || storagePath == null) {
            storagePath = STORAGE_DIRECTORY + "bang_detector_rec.mp4";
        }

        Log.i(this.getClass().getName(), "Storage Path: " + storagePath);
        recorder.setOutputFile(storagePath);
        this.context = context;
    }

    public void start() throws BangDetectorException {
        try {
            recorder.prepare();
            recorder.start();
            Log.i(this.getClass().getName(), "Recorder started ...");
            amplitudeListener = new AudioAmplitudeListener(recorder, 500, 10000, context);
            amplitudeListener.start();
        } catch (AudioAmplitudeListenerException | IOException ex) {
            throw new BangDetectorException(ex.getMessage());
        }
    }

    public void stop() {
        if (amplitudeListener != null) {
            amplitudeListener.stopMe();
        }
        recorder.stop();
        recorder.reset();
        recorder.release();
        Log.i(this.getClass().getName(), "Recorder stopped.");
    }
}
