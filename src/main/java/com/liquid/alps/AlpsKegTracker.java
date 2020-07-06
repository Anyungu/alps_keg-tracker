package com.liquid.alps;

import java.util.HashMap;

import com.liquid.alps.decoder.AlpsKegTrackerDecoder;
import com.liquid.alps.responses.AlpsKegTrackerResponse;

public class AlpsKegTracker {

    public HashMap<String, String> status;

    public HashMap<String, String> ssid;

    public HashMap<String, Float> coords;

    public String frameMode;

    public String fail;

    public AlpsKegTracker(String message) throws Exception {

        AlpsKegTrackerResponse decode = AlpsKegTrackerDecoder.decode(message);

        this.status = decode.getStatus();
        this.ssid = decode.getSsid();
        this.coords = decode.getCoords();
        this.fail = decode.getFail();
        this.frameMode = decode.getFrameMode();
    }

}