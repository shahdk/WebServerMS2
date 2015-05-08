package protocol;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

	public static Logger logger = Logger.getLogger("MyLog");
	static FileHandler fh;

	public static void setUp() {
		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("someLogFile.txt");
			logger.addHandler(fh);
			// logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
