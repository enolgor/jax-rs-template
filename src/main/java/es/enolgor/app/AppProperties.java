package es.enolgor.app;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import es.enolgor.configuration.Configuration;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

public class AppProperties {
	
	private static final AppDirs appDirs = AppDirsFactory.getInstance();
	private static final Properties appProperties = new Properties();
	static {
		try {
			appProperties.load(Configuration.class.getResourceAsStream("/system.default.properties"));
			
			String userConfigDir = appDirs.getUserConfigDir(getAppName(), null, getAppAuthor());
			String userDataDir = appDirs.getUserDataDir(getAppName(), null, getAppAuthor());
			String userLogDir = Paths.get(userDataDir, "logs").toString();
			String userLogFile = Paths.get(userLogDir, getAppName()+".log").toString();
			
			appProperties.put("app.configuration.dir", userConfigDir);
			appProperties.put("app.data.dir", userDataDir);
			appProperties.put("app.log.dir", userLogDir);
			appProperties.put("app.log.file", userLogFile);
		} catch (IOException e) {
			// NEVER SHOULD HAPPEN
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*
		From de appdir library:
		On Mac OS X : /Users/<Account>/Library/Application Support/<AppName>
		On Windows XP : C:\Documents and Settings\<Account>\Application Data\Local Settings\<AppAuthor>\<AppName>
		On Windows 7 : C:\Users\<Account>\AppData\<AppAuthor>\<AppName>
		On Unix/Linux : /home/<account>/.local/share/<AppName>
	 */
	
	public static String getAppAuthor() {
		return appProperties.getProperty("app.author");
	}
	
	public static String getAppName() {
		return appProperties.getProperty("app.name");
	}
	
	public static String getAppConfigurationDir() {
		return appProperties.getProperty("app.configuration.dir");
	}
	
	public static String getAppDataDir() {
		return appProperties.getProperty("app.data.dir");
	}
	
	public static String getAppLogDir() {
		return appProperties.getProperty("app.log.dir"); 
	}
	
	public static String getAppLogFile() {
		return appProperties.getProperty("app.log.file"); 
	}
	
	public static Properties getProperties() {
		return appProperties;
	}
}
