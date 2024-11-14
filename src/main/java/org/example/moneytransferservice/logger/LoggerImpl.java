package org.example.moneytransferservice.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LoggerImpl implements Logger {
    private static Logger instance = null;
    private FileWriter writer;

    private LoggerImpl() {
        try {
            writer = new FileWriter("log.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new LoggerImpl();
        }
        return instance;
    }

    public void log(String msg) {
        try {
            writer.append("[")
                    .append(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                    .append(":")
                    .append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss.nnn")))
                    .append("] ")
                    .append(msg)
                    .append("\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Журнал ошибок " + msg);
        }
    }
}