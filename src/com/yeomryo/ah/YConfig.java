package com.yeomryo.ah;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class YConfig {
	
	public static YamlConfiguration getYMLforsave(File path, String file){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(path,"");
		if(!f.exists())
			f.mkdirs();
		f = new File(path,file);
		try {
			if(f.exists())
				f.delete();
			f.createNewFile();	
			yc.load(f);
			
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}

	public static YamlConfiguration getYML(File path, String file){
		YamlConfiguration yc= new YamlConfiguration();
		File f = new File(path,"");
		if(!f.exists())
			f.mkdirs();
		f = new File(path,file);
		try {
			if(!f.exists())
					f.createNewFile();
			yc.load(f);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yc;
	}
	
}
