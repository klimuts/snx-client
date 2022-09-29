package com.klimuts.snxgui.util;

import javafx.concurrent.Task;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskUtils {

    public void startDelayedTask(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException ignored) {}
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

}
