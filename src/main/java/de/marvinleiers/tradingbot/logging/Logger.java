package de.marvinleiers.tradingbot.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public void log(String text)
    {
        log(LogLevel.INFO, text);
    }

    public void log(LogLevel level, String text)
    {
        System.out.println("[" + dateFormat.format(new Date(System.currentTimeMillis())) + "] [" + level.name() + "]: " + text);
    }
}
