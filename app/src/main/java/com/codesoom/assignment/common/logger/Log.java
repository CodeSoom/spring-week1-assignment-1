package com.codesoom.assignment.common.logger;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public final class Log extends Formatter {

    private String className;
    private static volatile Log _instance = null;

    public static Log getInstance() {
        if (_instance == null) {
            /*
                 동시성 이슈를 해소 하기 위해 싱글턴 패턴에  암묵적인 lock(synchronized)을 적용
             */
            synchronized (Log.class) {
                if (_instance == null) {
                    _instance = new Log();

                }
            }
        }
        return _instance;
    }


    @Override
    public String format(LogRecord record) {

        StringBuffer buf = new StringBuffer(1000);
        buf.append(calcDate());

        buf.append(" [");
        buf.append(record.getLevel());
        buf.append("] ");

        buf.append("[");
        buf.append(className);
        buf.append(" ");
        buf.append(record.getSourceMethodName());
        buf.append("] ");

        buf.append(record.getMessage());
        buf.append("\n");


        return buf.toString();
    }

    public Logger getLog(Class<?> className) {
        this.className = className.getName();
        Logger logger = Logger.getGlobal();
        Handler handler = new ConsoleHandler();
        handler.setFormatter(_instance);
        logger.addHandler(handler);
        return logger;
    }

    private String calcDate() {
        String loggerTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return loggerTime;
    }
}
