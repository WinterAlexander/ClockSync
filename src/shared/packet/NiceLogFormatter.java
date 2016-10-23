package shared.packet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * <p>Undocumented :(</p>
 *
 * <p>Created by Alexander Winter on 2016-10-23.</p>
 */
public class NiceLogFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		if(record.getThrown() == null)
		{
			return record.getLoggerName() + ": " +
					new Date(record.getMillis()) +
					" [" + record.getLevel() + "] " +
					formatMessage(record) +
					System.getProperty("line.separator");
		}

		StringWriter stringWriter = new StringWriter();
		PrintWriter stream = new PrintWriter(stringWriter);

		record.getThrown().printStackTrace(stream);

		return record.getLoggerName() + ": " +
				new Date(record.getMillis()) +
				" [" + record.getLevel() + "] " +
				formatMessage(record) +
				System.getProperty("line.separator") +
				stringWriter.toString() +
				System.getProperty("line.separator");
	}
}
