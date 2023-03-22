package api.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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

		// file이 존재하지 않으면 createFile
		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
			} catch (IOException e) {
				LogUtil.error(logger, "Error, Failed to create file");
			}
		}

		/*
		 * stream을 사용하여 fileList의 값을 \n 개행 후
		 * getBytes -> String을 byte로 변경 
		 * StandardOpenOption.APPEND 를 사용하여 append
		 */
		fileList.stream().forEach(t -> {
			try {
				Files.write(filePath, (t + "\n").getBytes(), StandardOpenOption.APPEND); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		/*
		 * stream의 filter의 endsWith로 리스트 내 37.txt로 끝나는 값들로 이루어진 List로 데이터 
		 */
		// List<String> filterList = fileList.stream().filter(t -> t.endsWith("37.txt")).collect(Collectors.toList());
		// System.out.println(filterList);


		/*
		 * StandardOpenOption.CREATE 사용
		 * collect(Collectors.joinning("\n"))은 List에 있는 값들을 \n 개행을 포함한 하나의 String으로 묶는 것
		 * 
		 * 이걸 쓰면 대용량 처리일 때 비효율적임
		 */
		// String fileContent = fileList.stream().collect(Collectors.joining("\n"));
		// try {
		// 	Files.write(filePath, fileContent.getBytes(), StandardOpenOption.CREATE);
		// } catch (IOException e) {
		// 	e.printStackTrace();
		// }
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
