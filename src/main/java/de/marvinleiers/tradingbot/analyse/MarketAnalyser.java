package de.marvinleiers.tradingbot.analyse;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.trend.Trend;

public class MarketAnalyser extends Thread
{
    private boolean owningCrypto;
    private float priceBought;
    private final String symbol;

    public MarketAnalyser(String symbol)
    {
        symbol = symbol.toUpperCase();

        if (!symbol.matches("[A-Z0-9]+"))
            throw new UnsupportedOperationException("Symbol does not exist (" + symbol + ")");

        this.symbol = symbol;
        this.owningCrypto = false;
        this.priceBought = -1;
    }

    @Override
    public void run()
    {
        Main.getLogger().log("Analysing...");

        while (true)
        {
            Trend trend = Main.getTrendDecider().calculateTrend();
            float latestPrice = Main.getCache().getLatestPrice();

            if (trend == Trend.UP && !owningCrypto)
            {
                Main.getLogger().log("Buying BTC for " + latestPrice + " USD");
                owningCrypto = true;
            }
            else if (trend == Trend.DOWN && owningCrypto)
            {
                Main.getLogger().log("Selling BTC for " + latestPrice + "USD ("
                        + ((latestPrice - priceBought) / priceBought * 100) + "% profit)");
                owningCrypto = false;
            }
            else
            {
                Main.getLogger().log("No opportunity found...");
            }

            sleeping();
        }
    }

    private void sleeping()
    {
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
