package ch.sebooom.dump1090.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class LogFormatter extends Formatter {

	private static final String PATTERN = "dd-MM-yyyy HH:mm:ss.SSSXXX";
	
	@Override
	public String format(LogRecord record) {


		return String.format(
                "%4$s %1$s %2$-7s %3$s %n",
                new SimpleDateFormat(PATTERN).format(
                        new Date(record.getMillis())),
                record.getLevel().getName(), formatMessage(record),
				"[" + Thread.currentThread().getName() + "]");
	}

}
