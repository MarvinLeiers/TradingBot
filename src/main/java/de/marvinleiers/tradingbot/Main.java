package de.marvinleiers.tradingbot;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import de.marvinleiers.tradingbot.datamining.CandlestickCache;
import de.marvinleiers.tradingbot.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main
{
    private static Logger logger;
    private static CandlestickCache cache;
    private static final CandlestickInterval interval = CandlestickInterval.ONE_MINUTE;

    public static void main(String[] args)
    {
        logger = new Logger();

        System.out.println(" _____              _ _             ____        _");
        System.out.println("|_   __ __ __ _  __| (_)_ __   __ _| __ )  ___ | |_");
        System.out.println("  | || '__/ _` |/ _` | | '_ \\ / _` |  _ \\ / _ \\| __|");
        System.out.println("  | || | | (_| | (_| | | | | | (_| | |_) | (_) | |_");
        System.out.println("  |_||_|  \\__,_|\\__,_|_|_| |_|\\__, |____/ \\___/ \\__|");
        System.out.println("                              |___/");
        System.out.println("\t\t\t\t  by Marvin Leiers");
        System.out.println(" ");

        cache = new CandlestickCache("BTCUSDT", CandlestickInterval.ONE_MINUTE);
        cache.start();
    }

    public static CandlestickCache getCache()
    {
        return cache;
    }

    public static Logger getLogger()
    {
        return logger;
    }
}
