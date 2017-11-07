package com.demo.homeautomation;

import ai.api.model.AIError;
import ai.api.model.AIResponse;

/**
 * Created by administrator05 on 5/11/17.
 */
public interface AIListener {
    void onResult(AIResponse result); // here process response
    void onError(AIError error); // here process error
    void onAudioLevel(float level); // callback for sound level visualization
    void onListeningStarted(); // indicate start listening here
    void onListeningCanceled(); // indicate stop listening here
    void onListeningFinished(); // indicate stop listening here
}