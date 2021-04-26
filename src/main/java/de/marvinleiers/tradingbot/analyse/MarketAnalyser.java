package de.marvinleiers.tradingbot.analyse;

import com.binance.api.client.domain.market.Candlestick;
import de.marvinleiers.tradingbot.Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MarketAnalyser extends Thread
{
    private final String symbol;

    public MarketAnalyser(String symbol)
    {
        symbol = symbol.toUpperCase();

        if (!symbol.matches("[A-Z0-9]+"))
            throw new UnsupportedOperationException("Symbol does not exist (" + symbol + ")");

        this.symbol = symbol;
    }

    @Override
    public void run()
    {
        List<Candlestick> candlesticks = Main.getCache().getCandlestickTimeFrame(0, System.currentTimeMillis());

        Main.getLogger().log("Analysing...");

        while (true)
        {
            //TODO: analyse market
        }
    }
}
