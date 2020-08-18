package net.dohaw.play.bossbarmsg;

import me.c10coding.coreapi.BetterJavaPlugin;

import java.io.File;

public final class BossbarMsg extends BetterJavaPlugin {

    @Override
    public void onEnable() {

        hookAPI(this);

        validateConfigs();
        registerEvents(new BossbarSetter(this));
    }

    @Override
    public void onDisable() {
    }

    public void validateConfigs(){
        File[] files = {new File(this.getDataFolder(), "config.yml")};
        for(File f : files){
            if(!f.exists()){
                this.saveResource(f.getName(), false);
            }
        }
    }

}
