package api.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class DeleteContent {
	private static Logger logger = LoggerFactory.getLogger(CreateContent.class);

	// xtorm Connection 선언 및 초기화
	public asysConnectData conn = null;

	int seq = 0;
	
	public DeleteContent() {
		conn = new asysConnectData(Config.getConfig("XTORM.HOSTNAME"), Config.getIntConfig("XTORM.PORT") , Config.getConfig("XTORM.DESCRIPTION"), 
								   Config.getConfig("XTORM.ID"), Config.getConfig("XTORM.PASSWORD"));
	}

	// public DeleteContent(int seq) {
	// 	this.seq = seq;
	// }

	// public void run() {
	// 	logger.info(this.seq + "thread start.");

	// 	try {
	// 		// 1초 대기
	// 		Thread.sleep(1000);
	// 	} catch (InterruptedException e) {
	// 		logger.error("ERROR, {}", e.getMessage());
	// 	}
	// 	logger.info(this.seq + "thread end.");
	// }
	
	public void delete(){
		asysUsrElement uePage = new asysUsrElement(conn);
		uePage.m_elementId =  Config.getConfig("USER.GATEWAY") + "::" + Config.getConfig("DELETE.ELEMENTID") + "::" + Config.getConfig("USER.ECLASSID");
		int ret = uePage.delete(); // XTORM 엔진 삭제 호출 메소드
		
		if (ret != 0) {
			LogUtil.error(logger, "Error, delete failed, {0}", uePage.getLastError());
		} else {
			LogUtil.info(logger, "Success, delete normal, {0}", uePage.m_elementId);
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
