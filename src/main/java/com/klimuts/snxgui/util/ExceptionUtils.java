package com.klimuts.snxgui.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtils {

    public static Throwable findRootCause(Throwable e) {
        Throwable rootCause = e;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}
