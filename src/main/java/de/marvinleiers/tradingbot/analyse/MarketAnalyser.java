package de.marvinleiers.tradingbot.analyse;

import de.marvinleiers.tradingbot.Main;

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
        Main.getLogger().log("Analysing...");

        while (true)
        {
            Main.getLogger().log(Main.getTrendDecider().calculateTrend().name());

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
