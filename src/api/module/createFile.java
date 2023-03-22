package api.module;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class createFile {
	public static void main(String[] args) throws InterruptedException {
		
		for (int i=0; i < 100; i++) {
			String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	
			File file = new File("C:\\dev\\TestFolder\\" + filename + ".txt");
			try {
				if (file.createNewFile()) {
					System.out.println("File created");
				} else {
					System.out.println("File already exists");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			TimeUnit.SECONDS.sleep(1);
		}
        
	}
}
