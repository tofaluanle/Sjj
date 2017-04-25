package cn.sjj.engine;

import android.content.Context;
import android.os.RemoteException;

import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;

import cn.sjj.Logger;
import cn.sjj.util.AudioUtil;


/**
 * @auther 宋疆疆
 * @date 2015/7/7.
 */
public class SpeechEngine {

    private SpeechSynthesizer mTts;
    private boolean isSpeaking;

    public static SpeechEngine getInstance() {
        return SpeechEngineHolder.ourInstance;
    }

    private SpeechEngine() {
    }

    public void init(Context context) {
        AudioUtil.setVolume(context, AudioUtil.getMaxVolume(context));
        mTts = new SpeechSynthesizer(context, mTtsInitListener);
        setParam();
    }

    public void onDestroy() {
        mTts.stopSpeaking(mTtsListener);
        mTts.destory();
    }

    public int speak(String content) {
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking(mTtsListener);
        }
        int code = mTts.startSpeaking(content, mTtsListener);
        if (code != 0) {
            Logger.w("start speak error : " + code);
        }
        isSpeaking = true;
        return code;
    }

    public void stop() {
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking(mTtsListener);
        }
    }

    private void setParam() {
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, "local");
        mTts.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechSynthesizer.SPEED, "50");
        mTts.setParameter(SpeechSynthesizer.PITCH, "50");
        mTts.setParameter(SpeechSynthesizer.VOLUME, "100");
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    private static class SpeechEngineHolder {
        private static SpeechEngine ourInstance = new SpeechEngine();
    }

    private InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onInit(ISpeechModule arg0, int code) {
            Logger.i("InitListener init() code = " + code);
        }
    };

    private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
        @Override
        public void onBufferProgress(int progress) throws RemoteException {
        }

        @Override
        public void onCompleted(int code) throws RemoteException {
            Logger.i("onCompleted code =" + code);
            isSpeaking = false;
        }

        @Override
        public void onSpeakBegin() throws RemoteException {
            Logger.i("onSpeakBegin");
            isSpeaking = true;
        }

        @Override
        public void onSpeakPaused() throws RemoteException {
            Logger.i("onSpeakPaused.");
        }

        @Override
        public void onSpeakProgress(int progress) throws RemoteException {
        }

        @Override
        public void onSpeakResumed() throws RemoteException {
            Logger.i("onSpeakResumed.");
        }
    };
}
