package api.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class ReadListToCreateContent {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);

	public asysConnectData conn = null;

	public ReadListToCreateContent() {
		//asysConnectData초기화
		conn = new asysConnectData(Config.getConfig("XTORM.HOSTNAME"), Config.getIntConfig("XTORM.PORT") , Config.getConfig("XTORM.DESCRIPTION"), 
								   Config.getConfig("XTORM.ID"), Config.getConfig("XTORM.PASSWORD"));
	}

	public static void main(String[] args) {
		// start time
        long time = System.currentTimeMillis();
        // Base directory of project
        String baseDir = "./";

        // setting configuration
        try {
            Config.setConfig(baseDir + "conf/conf.properties");

            Config.setConfig("BASEDIR", baseDir);
            Config.setConfig("TIME", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(time)));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error(logger, "Error, Check your configuration properties -> {}", e.getMessage());
            return;
        }

		ReadListToCreateContent rltc = new ReadListToCreateContent();
		List<String> fileList = rltc.readFile();
		if (fileList == null || fileList.isEmpty()) {
			LogUtil.error(logger, "Error, fileList is empty! Check your fileList");
		} else {
			try {
				rltc.create(fileList);
			} finally {
				rltc.disconn();
			}
		}
	}

	public List<String> readFile() {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(Config.getConfig("CREATE.CREATELIST")));

		} catch (NullPointerException e) {
			LogUtil.error(logger, "Error, NullPointException! check your files -> {}", e.getMessage());
		} catch (IOException e) {
			LogUtil.error(logger, "Error, IOException! -> {} ", e.getMessage());
		}

		return lines;
	}

	public void create(List<String> fileList) {
		asysUsrElement uePage = new asysUsrElement(conn);
		for (String file : fileList) {
			uePage.m_localFile = file;
			uePage.m_descr = Config.getConfig("USER.DESCRIPTION");
			uePage.m_cClassId = Config.getConfig("USER.CCLASSID");
			uePage.m_userSClass = Config.getConfig("USER.USERSCLASS");
			uePage.m_eClassId = Config.getConfig("USER.ECLASSID");

			int ret = uePage.create(Config.getConfig("USER.GATEWAY"));

			if (ret != 0) {
				LogUtil.error(logger, "Error, create failed, {0}", uePage.getLastError());
			} else {
				LogUtil.info(logger, "Success, create normal, {0}", uePage.m_elementId);
			}
		}
	}

		// Connection 종료
		public void disconn() {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}
}
