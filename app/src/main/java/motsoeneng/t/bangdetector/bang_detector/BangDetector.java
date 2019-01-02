package motsoeneng.t.bangdetector.bang_detector;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class BangDetector {
    private AudioRecord audioRecord;
    private AudioAmplitudeListener amplitudeListener = null;
    private Context context;
    private static final int SAMPLE_RATE = 44100, AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO, AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSize;

    public BangDetector(Context context) {
        bufferSize = initRecordingBufferSize(4096);
        Log.i(this.getClass().getName(), "Buffer size: " + bufferSize);
        audioRecord = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AUDIO_ENCODING)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AUDIO_CHANNEL)
                        .build())
                .setBufferSizeInBytes(bufferSize)
                .build();
        this.context = context;
    }


    private int initRecordingBufferSize(int bufferSize) {
        int size = AudioRecord.getMinBufferSize(SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        if (size > bufferSize) {
            return size;
        }
        return bufferSize;
    }

    public void start() throws BangDetectorException {
        try {
            audioRecord.startRecording();
            Log.i(this.getClass().getName(), "Recorder started ...");
            amplitudeListener = new AudioAmplitudeListener(audioRecord, 500, 10000, context, bufferSize);
            amplitudeListener.start();
        } catch (AudioAmplitudeListenerException ex) {
            throw new BangDetectorException(ex.getMessage());
        }
    }

    public void stop() {
        if (amplitudeListener != null) {
            amplitudeListener.stopMe();
        }
        audioRecord.stop();
        audioRecord.release();
        Log.i(this.getClass().getName(), "Recorder stopped.");
    }
}
