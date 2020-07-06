package com.liquid.alps;

import com.liquid.alps.decoder.AlpsKegTrackerDecoder;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        AlpsKegTrackerDecoder decode = new AlpsKegTrackerDecoder();

        decode.decode("500ff54e17b8e865d4008821");
    }
}
