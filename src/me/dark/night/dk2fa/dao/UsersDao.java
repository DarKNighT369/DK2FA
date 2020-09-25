package me.dark.night.dk2fa.dao;

import me.dark.night.dk2fa.database.MySQL;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDao {

    private static Map<String, String> players;
    private static List<String> playersInVerification;

    static{
        players = new HashMap<>();
        playersInVerification = new ArrayList<>();
    }

    public static void add(String p, String secretId){
        if (!players.containsKey(p))
            players.put(p, secretId);
    }

    public static void remove(String p){
        players.remove(p);
    }

    public static String getSecretId(String p){
        return players.getOrDefault(p, "");
    }

    public static boolean whitelist(){
        return players.isEmpty();
    }

    public static boolean contains(String p){
        return players.containsKey(p);
    }

    public static void addInVerification(String p){
        playersInVerification.add(p);
    }

    public static void removeInVerification(String p){
        playersInVerification.remove(p);
    }

    public static boolean inVerification(String p){
        return playersInVerification.contains(p);
    }

    public static boolean verify(String p){
        return whitelist() ? contains(p) ? true : false : true;
    }

    public static void upToMySQL(){
        MySQL.openConnection();
        Bukkit.getConsoleSender().sendMessage("§e§l[DK2FA] §8» §aCarregando todos jogadores da Map");
        players.entrySet().forEach(entry -> MySQL.setPlayer(entry.getKey(), entry.getValue()));
        Bukkit.getConsoleSender().sendMessage("§e§l[DK2FA] §8» §aJogadores carregados com sucesso!");
        MySQL.closeConnection();
    }

    public static void upToMap(){
        MySQL.openConnection();
        Bukkit.getConsoleSender().sendMessage("§e§l[DK2FA] §8» §aCarregando todos jogadores do MySQL");
        for (Integer allID : MySQL.getAllIDS()) {
            String nick = MySQL.getNick(allID);
            String secretId = MySQL.getSecretID(allID);
            players.put(nick, secretId);
            MySQL.deleteId(allID);
        }
        Bukkit.getConsoleSender().sendMessage("§e§l[DK2FA] §8» §aJogadores carregados com sucesso!");
        MySQL.closeConnection();
    }

}
