package com.quiptmc2;

import com.quiptmc2.core.Quipt;
import com.quiptmc2.discord.Bot;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, QuiptMC2!");
        Quipt.INSTANCE.enable(Bot.instance());
    }
}
