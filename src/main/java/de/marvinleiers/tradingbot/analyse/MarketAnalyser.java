package de.marvinleiers.tradingbot.analyse;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.trend.Trend;

import java.text.DecimalFormat;

public class MarketAnalyser extends Thread
{
    private static final DecimalFormat formatter = new DecimalFormat("##0.00");
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
                priceBought = latestPrice;
                owningCrypto = true;
            }
            else if (trend == Trend.DOWN && owningCrypto)
            {
                float diff = (latestPrice - priceBought);
                double percentage = (diff / priceBought) * 100;
                String percentageFormatted = formatter.format(percentage);

                Main.getLogger().log("Selling BTC for " + latestPrice + "USD (" + percentageFormatted + "% profit)");

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
            sleep(1000 * 60 * 5);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
