package common;

import java.io.File;

public class GlobalConstants {
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String OS_NAME = System.getProperty("os.name");
	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static final String UPLOAD_FILE_FOLDER = PROJECT_PATH + File.separator +"uploadFiles"+ File.separator;

}
