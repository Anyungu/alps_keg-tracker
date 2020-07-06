package com.liquid.alps.decoder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liquid.alps.requests.WifiAcessModel;
import com.liquid.alps.requests.WifiRequestBody;
import com.liquid.alps.responses.AlpsKegTrackerResponse;
import com.liquid.alps.responses.LocationResponse;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlpsKegTrackerDecoder {

    public static AlpsKegTrackerResponse decode(String message) {

        AlpsKegTrackerResponse alpsKegTrackerResponse = new AlpsKegTrackerResponse();

        Integer length = message.length();

        if (length < 6) {

            HashMap<String, String> status = new HashMap<>();

            List<String> splitParts = splitParts(message, 2);

            String convertToBinaryString = convertToBinaryString(splitParts.get(0));

            alpsKegTrackerResponse.setFrameMode("status");

            switch (convertToBinaryString) {

                case "00":
                    status.put("Bat", "Almost Empty");
                    alpsKegTrackerResponse.setStatus(status);
                    break;

                case "01":
                    status.put("Bat", "Ok");
                    alpsKegTrackerResponse.setStatus(status);
                    break;

                case "02":
                    status.put("Bat", "Good");
                    alpsKegTrackerResponse.setStatus(status);
                    break;

                case "03":
                    status.put("Bat", "Excellent");
                    alpsKegTrackerResponse.setStatus(status);
                    break;

            }

            return alpsKegTrackerResponse;

        } else {

            HashMap<String, String> ssid = new HashMap<>();

            List<String> splitParts = splitParts(message, 12);

            ssid.put("x", "");
            ssid.put("y", "");

            List<String> splitPartsOne = splitParts(splitParts.get(0), 2);

            for (int i = 0; i < splitPartsOne.size(); i++) {

                if (i != splitPartsOne.size() - 1) {

                    ssid.put("x", ssid.get("x") + splitPartsOne.get(i) + ":");

                } else {

                    ssid.put("x", ssid.get("x") + splitPartsOne.get(i));

                }

            }

            List<String> splitPartsTwo = splitParts(splitParts.get(1), 2);

            for (int i = 0; i < splitPartsTwo.size(); i++) {

                if (i != splitPartsTwo.size() - 1) {

                    ssid.put("y", ssid.get("y") + splitPartsTwo.get(i) + ":");

                } else {

                    ssid.put("y", ssid.get("y") + splitPartsTwo.get(i));

                }

            }

            LocationResponse geolocate = geolocate(ssid);

            if (geolocate == null) {

                alpsKegTrackerResponse.setFail("fail");

            } else {
                HashMap<String, Float> coords = new HashMap<>();

                coords.put("lat", geolocate.getLocation().getLat());
                coords.put("lat", geolocate.getLocation().getLng());
                alpsKegTrackerResponse.setCoords(coords);
            }

            alpsKegTrackerResponse.setFrameMode("uplink");

            alpsKegTrackerResponse.setSsid(ssid);

            return alpsKegTrackerResponse;

        }
    }

    private static List<String> splitParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i = 0; i < len; i += partitionSize) {

            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }

    private static String convertToBinaryString(String string) {
        String binaryString = new BigInteger(string, 16).toString(2);

        Integer length = binaryString.length();
        if (length < 8) {
            for (int i = 0; i < 8 - length; i++) {
                binaryString = "0" + binaryString;
            }
        }
        return binaryString;
    }

    private static LocationResponse geolocate(HashMap<String, String> ssid) {

        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/geolocation/v1/geolocate")
                    .newBuilder();
            urlBuilder.addQueryParameter("key", "AIzaSyC5714tZfW4K_myvL6ytIJmjc40_7kaG8w");

            WifiRequestBody wifiRequestBody = new WifiRequestBody();
            wifiRequestBody.setConsiderIp("false");

            WifiAcessModel wifiAcessModelOne = new WifiAcessModel();
            wifiAcessModelOne.setMacAddress(ssid.get("x"));

            WifiAcessModel wifiAcessModelTwo = new WifiAcessModel();
            wifiAcessModelTwo.setMacAddress(ssid.get("y"));

            List<WifiAcessModel> wifiAcessModelList = new ArrayList<>();
            wifiAcessModelList.add(wifiAcessModelOne);
            wifiAcessModelList.add(wifiAcessModelTwo);
            wifiRequestBody.setWifiAccessPoints(wifiAcessModelList);

            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            String writeValueAsString = objectMapper.writeValueAsString(wifiRequestBody);

            RequestBody body = RequestBody.create(writeValueAsString, MediaType.parse("application/json"));

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).post(body).build();

            OkHttpClient client = new OkHttpClient();

            Call call = client.newCall(request);

            Response response = call.execute();

            if (response.code() == 200) {

                ObjectMapper responseObjectMapper = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                String string = response.body().string();

                LocationResponse readValue = responseObjectMapper.readValue(string, LocationResponse.class);

                return readValue;

            } else {

                return null;

            }

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }

}