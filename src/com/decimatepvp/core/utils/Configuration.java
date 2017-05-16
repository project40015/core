package com.decimatepvp.core.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration
{
	private String fileName = "defaultFile.yml";
	private JavaPlugin jp;
	private File file;
	private FileConfiguration fc;
	private String path;
	  
	public Configuration(JavaPlugin jp, String name)
	{
		this.jp = jp;
		this.fileName = name;
		this.path = "";
	}
	  
	public Configuration(JavaPlugin jp, String path, String name)
	{
		this.jp = jp;
		this.fileName = name;
		this.path = path;
	}
	
	public File getFile()
	{
		if(this.path != "") {
			this.file = new File(this.jp.getDataFolder().getPath() + path, this.fileName);
		}
		else {
			this.file = new File(this.jp.getDataFolder(), this.fileName);
		}
		return this.file;
	}
  
	public FileConfiguration getData()
	{
		createData();
		this.fc = YamlConfiguration.loadConfiguration(getFile());
		return this.fc;
	}
  
	public void saveData()
	{
		this.file = new File(this.jp.getDataFolder(), this.fileName);
		try
		{
			this.fc.save(getFile());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Attempting to fix error...");
			createData();
			saveData();
		}
	}
  
	public void createData() {
		if (!getFile().exists())
		{
			if (!this.jp.getDataFolder().exists()) {
				this.jp.getDataFolder().mkdirs();
			}
			if(this.jp.getResource("") != null) {
				this.jp.saveResource(this.fileName, false); 
			}
			else {
				try {
					this.file.createNewFile();
				} catch (IOException e) { }
			}
		}
	}

	public void reset() {
		File file = getFile();
		file.delete();
		createData();
	}
}
