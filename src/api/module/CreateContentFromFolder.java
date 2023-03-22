package api.module;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class CreateContentFromFolder {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);
	// xtorm Connection 선언 및 초기화
	// public asysConnectData conn = null;

	// public CreateContentFromFolder() {
	// 	//asysConnectData초기화
	// 	conn = new asysConnectData(Config.getConfig("XTORM.HOSTNAME"), Config.getIntConfig("XTORM.PORT") , Config.getConfig("XTORM.DESCRIPTION"), 
	// 							   Config.getConfig("XTORM.ID"), Config.getConfig("XTORM.PASSWORD"));
	// }

	// public void create() {
	// 	for (int j=0; j<Config.getIntConfig("CREATE.COUNT"); j++) {
	// 		asysUsrElement uePage = new asysUsrElement(conn);

	// 		uePage.m_localFile = Config.getConfig("USER.LOCALFILE");
	// 		uePage.m_descr = Config.getConfig("USER.DESCRIPTION");
	// 		uePage.m_cClassId = Config.getConfig("USER.CCLASSID");
	// 		uePage.m_userSClass = Config.getConfig("USER.USERSCLASS");
	// 		uePage.m_eClassId = Config.getConfig("USER.ECLASSID");

	// 		int ret = uePage.create(Config.getConfig("USER.GATEWAY"));
			
	// 		if (ret != 0) {
	// 			LogUtil.error(logger, "Error, create failed, {0}", uePage.getLastError());
	// 		} else {
	// 			LogUtil.info(logger, "Success, create normal, {0}", uePage.m_elementId);
	// 		}
	// 	}
    // }

	// 경로안에 있는 파일이름을 List에 넣기
	public List<String> getFileNameList() {
		String folderPath = Config.getConfig("USER.FOLDERPATH");
		List<String> fileNameList = new ArrayList<>();
		Path dir = Paths.get(folderPath);

		/*
		 * Files.walk() -> 폴더 내 폴더까지 들어가서 파일리스트 가져온다.(stream 타입)
		 * Files.list() -> 폴더 내 파일리스트를 가져온다. (Stream 타입)
		 */
		try {
			// try (Stream<Path> streamPaths = Files.walk(dir)) {
			// 	streamPaths.filter(Files::isRegularFile).forEach(System.out::println);
			// }
			try(Stream<Path> stream = Files.list(dir)) {
				stream.filter(Files::isRegularFile).forEach(t -> fileNameList.add(t.normalize().toString()));
				// stream.filter(t -> Files.isRegularFile(t)).forEach(t -> System.out.println(t));
				
			}
		} catch (IOException e) {
			LogUtil.error(logger, "Error, No files exist.", e.getMessage());
		}
		return fileNameList;
	}

	public void writeToSamFile(List<String> fileList) {
		Path filePath = Paths.get(Config.getConfig("DOWNLOAD.DOWNLOADLIST"));

		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
			} catch (IOException e) {
				LogUtil.error(logger, "Error, Failed to create file");
			}
		}

		fileList.stream().forEach(t -> {
			try {
				Files.write(filePath, (t + "\n").getBytes(), StandardOpenOption.APPEND); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	// Connection 종료
	// public void disconn() {
	// 	if (conn != null) {
	// 		conn.close();
	// 		conn = null;
	// 	}
	// }

	public static void main(String[] args) {
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

		CreateContentFromFolder ccff = new CreateContentFromFolder();
		List<String> fileList = ccff.getFileNameList();
		ccff.writeToSamFile(fileList);
	}
}
