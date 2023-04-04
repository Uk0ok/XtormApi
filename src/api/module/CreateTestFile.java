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

public class CreateTestFile {
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
		
		new CreateTestFile().createFiles();

		// logging end
		LogUtil.info(logger, "End Create Api. ( {0}s )", CommonUtil.getTimeElapsed(time));
	}

	public void createFiles() {
		for (int i=0; i < Config.getIntConfig("CREATE.COUNT"); i++) {
			String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			filename += String.valueOf(i);
			String folderPath = Config.getConfig("USER.FOLDERPATH");
			// Path dir = Paths.get(folderPath);
			Path createFileName = Paths.get(folderPath + "/" + filename + ".txt");

			if(Files.notExists(createFileName)) {
				try {
					Files.write(createFileName, ("create file" + filename + "\n").getBytes(), StandardOpenOption.CREATE);
					LogUtil.info(logger, "File Created, {}", createFileName);
					} catch (IOException e) {
						LogUtil.error(logger, "Error, Failed to create or write File {}", e.getMessage());
						return;
					}
			}
			/*
			try {
				// 이걸로 filename을 변경하는 건 충돌 가능성이 있다.
				// TimeUnit.SECONDS.sleep(1); 
				
			} catch (InterruptedException e) {
				// Exception 터져서 꺼지는걸 방지 하기 위함의 try catch 문이고
				// 코드에 큰 영향을 주지않아서 별다른 처리 X
			}
			 */
		}
	}
}
