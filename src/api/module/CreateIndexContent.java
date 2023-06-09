package api.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class CreateIndexContent {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);
	// xtorm Connection 선언 및 초기화
	public asysConnectData conn = null;

	public CreateIndexContent() {
		conn = new asysConnectData(Config.getConfig("XTORM.HOSTNAME"), Config.getIntConfig("XTORM.PORT") , Config.getConfig("XTORM.DESCRIPTION"), 
								   Config.getConfig("XTORM.ID"), Config.getConfig("XTORM.PASSWORD"));
	}

	public void create() {
		asysUsrElement uePage = new asysUsrElement(conn);

		uePage.m_localFile = Config.getConfig("USER.LOCALFILE");
		uePage.m_descr = Config.getConfig("USER.DESCRIPTION");
		uePage.m_cClassId = Config.getConfig("USER.CCLASSID");
		uePage.m_userSClass = Config.getConfig("USER.USERSCLASS");
		uePage.m_eClassId = Config.getConfig("USER.ECLASSID");

		uePage.addIndexValue("TEST", "FILE_NAME", "test.txt");
		uePage.addIndexValue("TEST", "FILE_EXTENSION", "txt");
		uePage.addIndexValue("TEST", "USER_ID", "uk0ok");

		int ret = uePage.create(Config.getConfig("USER.GATEWAY"));
			
		if (ret != 0) {
			LogUtil.error(logger, "Error, create failed, {0}", uePage.getLastError());
		} else {
			LogUtil.info(logger, "Success, create normal, {0}", uePage.m_elementId);
		}
		
		disconn(); 
	}

	public void disconn() {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
}

