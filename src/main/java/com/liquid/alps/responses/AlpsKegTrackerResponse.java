package com.liquid.alps.responses;

import java.util.HashMap;

public class AlpsKegTrackerResponse {

    HashMap<String, String> status;

    HashMap<String, String> ssid;

    HashMap<String, Float> coords;

    private String frameMode;

    private String fail;

    public HashMap<String, String> getStatus() {
        return status;
    }

    public void setStatus(HashMap<String, String> status) {
        this.status = status;
    }

    public HashMap<String, String> getSsid() {
        return ssid;
    }

    public void setSsid(HashMap<String, String> ssid) {
        this.ssid = ssid;
    }

    public HashMap<String, Float> getCoords() {
        return coords;
    }

    public void setCoords(HashMap<String, Float> coords) {
        this.coords = coords;
    }

    public String getFrameMode() {
        return frameMode;
    }

    public void setFrameMode(String frameMode) {
        this.frameMode = frameMode;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

}