package live.qsmc.quipt.discord2;

import live.qsmc.quipt.core.Quipt;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, QuiptMC2!");
        Quipt.INSTANCE.enable(Bot.instance());
    }
}
