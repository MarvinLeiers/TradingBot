package de.marvinleiers.tradingbot.analyse;

import com.binance.api.client.domain.market.Candlestick;
import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.indicators.SimpleMovingAverage;

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
            System.out.println(Main.getTrendDecider().calculateTrend());

            try
            {
                sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
