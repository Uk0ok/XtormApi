package api.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class DownloadContent {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);

	// xtorm Connection 선언 및 초기화
	public asysConnectData conn = null;

	int seq = 0;
	
	public DownloadContent () {
		//asysConnectData초기화
		conn = new asysConnectData(Config.getConfig("XTORM.HOSTNAME"), Config.getIntConfig("XTORM.PORT") , Config.getConfig("XTORM.DESCRIPTION"), 
								   Config.getConfig("XTORM.ID"), Config.getConfig("XTORM.PASSWORD"));
	}
	
	public void download() {
		asysUsrElement uePage = new asysUsrElement(conn);
		uePage.m_elementId = Config.getConfig("USER.GATEWAY") + "::" + Config.getConfig("DOWNLOAD.ELEMENTID") + "::" + Config.getConfig("USER.ECLASSID");

		int ret = uePage.getContent(Config.getConfig("DOWNLOAD.DOWNPATH") + Config.getConfig("DOWNLOAD.ELEMENTID"));
		
		if (ret != 0) {
			LogUtil.error(logger, "Error, download failed, {0}", uePage.getLastError());
		} else {
			LogUtil.info(logger, "Success, download normal, {0}", uePage.m_elementId);
		}
		
		disconn();
    }

	// Connection 종료
	private void disconn() {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
}

