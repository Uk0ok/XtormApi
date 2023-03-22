import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.module.CreateContent;
import api.module.CreateIndexContent;
import api.module.DeleteContent;
import api.module.DownloadContent;
import uk0ok.util.CommonUtil;
import uk0ok.util.Config;
import uk0ok.util.LogUtil;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) { 
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
            Config.setConfig("TIME", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(time)));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error(logger, "Error, Check your configuration properties -> {}", e.getMessage());
            return;
        }
        // process start
        // CreateContent cc = new CreateContent();
        // try {
        //     cc.create();
        // } finally {
        //     cc.disconn();
        // }

        // File downPath = new File(Config.getConfig("DOWNLOAD.DOWNPATH"));
		// if (!downPath.exists()) {
		// 	downPath.mkdirs();
		// }
        // DownloadContent dc = new DownloadContent();
        // try {
        //     dc.download();
        // } finally {
        //     dc.disconn();
        // }

        // DeleteContent dec = new DeleteContent();
        // try {
        //     dec.delete();
        // } finally {
        //     dec.disconn();
        // }

        CreateIndexContent cic = new CreateIndexContent();
        try {
            cic.create();
        } finally {
            cic.disconn();
        }

        // logging end
        LogUtil.info(logger, "End Create Api. ( {0}s )", CommonUtil.getTimeElapsed(time));
    }
}