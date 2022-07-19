package com.klimuts.snxgui.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionInfo {

    private String officeModeIP;
    private String dnsServer;
    private String secondaryDnsServer;
    private String dnsSuffix;
    private String timeout;

}
