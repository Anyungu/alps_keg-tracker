package com.liquid.alps.requests;

import java.util.List;

public class WifiRequestBody {

    private String considerIp;

    private List<WifiAcessModel> wifiAccessPoints;

    public String getConsiderIp() {
        return considerIp;
    }

    public void setConsiderIp(String considerIp) {
        this.considerIp = considerIp;
    }

    public List<WifiAcessModel> getWifiAccessPoints() {
        return wifiAccessPoints;
    }

    public void setWifiAccessPoints(List<WifiAcessModel> wifiAccessPoints) {
        this.wifiAccessPoints = wifiAccessPoints;
    }

}