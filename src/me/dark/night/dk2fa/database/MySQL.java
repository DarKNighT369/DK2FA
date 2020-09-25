package me.dark.night.dk2fa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import me.dark.night.dk2fa.Main;

public class MySQL {

    public static Connection con = null;

    public static void openConnection() {
        String user = Main.getInstance().getConfig().getString("MySQL.user");
        String password = Main.getInstance().getConfig().getString("MySQL.senha");
        String ip = Main.getInstance().getConfig().getString("MySQL.ip");
        Integer porta = Main.getInstance().getConfig().getInt("MySQL.porta");
        String database = Main.getInstance().getConfig().getString("MySQL.database");
        String url = "jdbc:mysql://" + ip + ":" + porta + "/" + database + "?autoReconnect=true";
        try {
            con = DriverManager.getConnection(url, user, password);
            Bukkit.getConsoleSender()
                    .sendMessage("§e§l[DK2FA] §8» §aConexão com MySQL concedida com sucesso.");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender()
                    .sendMessage("§e§l[DK2FA] §8» §cA conexão com o MySQL não foi possivel.");
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
        }
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                Bukkit.getConsoleSender()
                        .sendMessage("§e§l[DK2FA] §8» §aConexão do MySQL fechada com sucesso");
            } catch (SQLException e) {
                Bukkit.getConsoleSender()
                        .sendMessage("§e§l[DK2FA] §8» §aNão foi possivel fechar a conexão com o MySQL");
            }
        }

    }

    public static void createTable() {
        if (con != null) {
            PreparedStatement stm = null;
            try {
                stm = con.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `DK2FA` (`id` INT NOT NULL AUTO_INCREMENT, `player` VARCHAR(24) NULL, `secretid` TEXT NULL, PRIMARY KEY (`id`));");

                stm.executeUpdate();
                Bukkit.getConsoleSender().sendMessage("§e§l[DK2FA] §8» §aTabela criada com sucesso.");
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("§e§l[DK2FA] §8» §aNão foi possivel criar a tabela.");
            }
        }
    }

    public static boolean containsPlayer(String p) {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("SELECT * FROM `DK2FA` WHERE `player` = ?");
            stm.setString(1, p);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }

    }

    public static void setPlayer(String p, String secretid) {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("INSERT INTO `DK2FA`(`player`, `secretid`) VALUES (?,?)");
            stm.setString(1, p);
            stm.setString(2, secretid);
            stm.executeUpdate();
            Bukkit.getConsoleSender()
                    .sendMessage("§e§l[DK2FA] §8» §aPlayer §f" + p + "§a foi criado com sucesso!");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(
                    "§e§l[DK2FA] §8» §cNão foi possivel inserir o player: §f" + p + "§c no banco de dados!");
        }
    }

    public static String getSecretID(int id) {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("SELECT * FROM `DK2FA` WHERE `id` = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                return rs.getString("secretid");
            }
            return "";
        } catch (Exception e) {
            Bukkit.getConsoleSender()
                    .sendMessage("§e§l[DK2FA] §8» §cNão foi possivel pegar o secretid do id §f" + id);
            return "";
        }
    }

    public static String getNick(int id) {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("SELECT * FROM `DK2FA` WHERE `id` = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                return rs.getString("player");
            }
            return "";
        } catch (Exception e) {
            Bukkit.getConsoleSender()
                    .sendMessage("§e§l[DK2FA] §8» §cNão foi possivel pegar o nick do id §f" + id);
            return "";
        }
    }

    public static void deleteId(int id) {
        PreparedStatement stm = null;
        try {
            stm = con.prepareStatement("DELETE FROM `DK2FA` WHERE `id` = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(
                    "§e§l[DK2FA] §8» §cNão foi possivel remover o id " + id + " do banco de dados.");
        }
    }

    public static List<Integer> getAllIDS() {
        PreparedStatement stm = null;
        List<Integer> ids = new ArrayList<>();
        try {
            stm = con.prepareStatement("SELECT * FROM `DK2FA`");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender()
                    .sendMessage("§e§l[DK2FA] §8» §cOcorreu um erro ao carregar todos os ids!");
        }
        return ids;
    }

}
