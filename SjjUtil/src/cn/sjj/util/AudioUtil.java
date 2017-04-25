package cn.sjj.util;

import android.content.Context;
import android.media.AudioManager;

public class AudioUtil {

    private static final int DEFAULT_TYPE = AudioManager.STREAM_MUSIC;
    private static final int DEFAULT_FLAG = AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE;

    public static void up(Context context) {
        AudioManager am = getAudioManager(context);
        am.adjustStreamVolume(DEFAULT_TYPE, AudioManager.ADJUST_RAISE, DEFAULT_FLAG);
    }

    public static void down(Context context) {
        AudioManager am = getAudioManager(context);
        am.adjustStreamVolume(DEFAULT_TYPE, AudioManager.ADJUST_LOWER, DEFAULT_FLAG);
    }

    public static void setVolume(Context context, int index) {
        AudioManager am = getAudioManager(context);
        am.setStreamVolume(DEFAULT_TYPE, index, DEFAULT_FLAG);
    }

    public static int getMaxVolume(Context context) {
        AudioManager am = getAudioManager(context);
        return am.getStreamMaxVolume(DEFAULT_TYPE);
    }

    public static int getVolume(Context context) {
        AudioManager am = getAudioManager(context);
        return am.getStreamVolume(DEFAULT_TYPE);
    }

    public static void setMicrophoneMute(Context context, boolean on) {
        AudioManager am = getAudioManager(context);
        am.setMicrophoneMute(on);
    }

    private static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
}
