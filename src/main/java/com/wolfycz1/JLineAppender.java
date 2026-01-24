package com.wolfycz1;

import ch.qos.logback.core.ConsoleAppender;
import org.jline.reader.LineReader;

public class JLineAppender<E> extends ConsoleAppender<E> {
    private static LineReader lineReader;

    public static void setLineReader(LineReader lineReader) {
        JLineAppender.lineReader = lineReader;
    }

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
