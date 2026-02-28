package com.wolfycz1;

import ch.qos.logback.core.ConsoleAppender;
import org.jline.reader.LineReader;

/**
 * A custom Logback appender designed to integrate with JLine3.
 * @param <E> The type of log event being appended.
 * @author woflycz1
 */
public class JLineAppender<E> extends ConsoleAppender<E> {
    private static LineReader lineReader;

    /**
     * Injects the active JLine reader into the logging configuration.
     * Must be called during game initialization before any logs are expected to print.
     * @param lineReader The LineReader instance managing console input.
     */
    public static void setLineReader(LineReader lineReader) {
        JLineAppender.lineReader = lineReader;
    }

    /**
     * Processes and outputs the log event.
     * @param eventObject The logging event to be processed.
     */
    @Override
    protected void append(E eventObject) {
        if (lineReader == null) {
            super.append(eventObject);
            return;
        }

        byte[] byteArray = this.encoder.encode(eventObject);
        String logMsg = new String(byteArray);

        lineReader.printAbove(logMsg);
    }
}
