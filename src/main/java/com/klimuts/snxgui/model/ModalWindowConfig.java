package com.klimuts.snxgui.model;

import com.klimuts.snxgui.model.enums.ModalWindowType;
import javafx.stage.Stage;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.Callable;

@Data
@Builder
public class ModalWindowConfig {

    private Stage parentStage;
    private ModalWindowType windowType;
    private boolean closeOnMaskClick;
    private Callable<Boolean> closeCallback;
    private String errorMessage;

}
