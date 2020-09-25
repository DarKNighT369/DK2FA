package me.dark.night.dk2fa.utils;

import me.dark.night.dk2fa.Main;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    public static List<String> messageJoin;

    public Config(Main main){
        messageJoin = main.getConfig().getStringList("Mensagens.Entrou");
        messageJoin = messageJoin.stream().map(l -> l.replace('&', '§')).collect(Collectors.toList());
    }

}
