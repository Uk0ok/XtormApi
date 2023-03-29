package api.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk0ok.util.CommonUtil;
import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class createFile {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);
	public static void main(String[] args) throws InterruptedException {
		// logging
        LogUtil.info(logger, "Start Create Api");
        // start time
        long time = System.currentTimeMillis();
        // Base directory of project
		String baseDir = "./";
		// setting configuration
        try {
            Config.setConfig(baseDir + "conf/conf.properties");

            Config.setConfig("BASEDIR", baseDir);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error(logger, "Error, Check your configuration properties -> {}", e.getMessage());
            return;
        }
		
		new createFile().createFiles();

		// logging end
		LogUtil.info(logger, "End Create Api. ( {0}s )", CommonUtil.getTimeElapsed(time));
	}

	public void createFiles() throws InterruptedException {
		System.out.println();
		for (int i=0; i < 100; i++) {
			String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String folderPath = Config.getConfig("USER.FOLDERPATH");
			// Path dir = Paths.get(folderPath);
			Path createFileName = Paths.get(folderPath + "/" + filename + ".txt");
			
			if(Files.notExists(createFileName)) {
				try {
					Files.createFile(createFileName);
					LogUtil.info(logger, "File Created");
					try {
						Files.write(createFileName, ("create file" + filename + "\n").getBytes(), StandardOpenOption.APPEND);
					} catch (IOException e) {
						LogUtil.error(logger, "Error, Failed to write File {}", e.getMessage());
					}
				} catch (IOException e) {
					LogUtil.error(logger, "Error, Failed to create file {}", e.getMessage());
				}
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
