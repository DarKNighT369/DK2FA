package me.dark.night.dk2fa.process;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import me.dark.night.dk2fa.Main;
import me.dark.night.dk2fa.dao.UsersDao;

public class UserProcess {

    public UserProcess(Main main){
        GoogleAuthenticator gAuth = new GoogleAuthenticator(new GoogleAuthenticatorConfig());
        if (main.getConfig().getBoolean("Config.Whitelist"))
            main.getConfig().getStringList("Jogadores").forEach(nick -> UsersDao.add(nick, gAuth.createCredentials().getKey()));
    }

}
