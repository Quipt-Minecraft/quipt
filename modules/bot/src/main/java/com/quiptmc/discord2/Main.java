package com.quiptmc.discord2;

import com.quiptmc.core2.Quipt;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, QuiptMC2!");
        Quipt.INSTANCE.enable(Bot.instance());
    }
}
