package api.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class DeleteFromFile {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);

	public  asysConnectData conn = null;

	public DeleteFromFile() {
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
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error(logger, "Error, Check your configuration properties -> {}", e.getMessage());
            return;
        }
		DeleteFromFile dff = new DeleteFromFile();
		List<String> elementIdList = dff.readElementId();
		if (elementIdList == null || elementIdList.isEmpty()) {
			LogUtil.error(logger, "Error, elementIdList is empty! Check your elementIdList");
		} else {
			try {
				dff.deleteFiles(elementIdList);
			} finally {
				dff.disconn();
			}
		}
		
	}

	public void deleteFiles(List<String> elementIdList) {
		asysUsrElement uePage = new asysUsrElement(conn);
		for (String elementId : elementIdList) {
			uePage.m_elementId = Config.getConfig("USER.GATEWAY") + "::" + elementId + "::" + Config.getConfig("USER.ECLASSID");
			
			int ret = uePage.delete();
			
			if (ret != 0) {
				LogUtil.error(logger, "Error, delete failed, {0}", uePage.getLastError());
			} else {
				LogUtil.info(logger, "Success, delete normal, {0}", uePage.m_elementId);
			}
		}
	}

	public List<String> readElementId() {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(Config.getConfig("DELETE.DELETELIST")));
		} catch (NullPointerException e) {
			LogUtil.error(logger, "Error, NullPointException! check your files -> {}", e.getMessage());
		} catch (IOException e) {
			LogUtil.error(logger, "Error, IOException! -> {} ", e.getMessage());
		}

		return lines;
	}

	// Connection 종료
	public void disconn() {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
}
