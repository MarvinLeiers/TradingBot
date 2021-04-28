package de.marvinleiers.tradingbot.analyse;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.trend.Trend;
import de.marvinleiers.tradingbot.strategies.Strategy;
import de.marvinleiers.tradingbot.strategies.WMA9OverMA50Strategy;

import java.text.DecimalFormat;

public class MarketAnalyser extends Thread
{
    private static final DecimalFormat formatter = new DecimalFormat("##0.00");
    private boolean owningCrypto;
    private float priceBought;
    private int trades;
    private double profit;
    private final String symbol;

    public MarketAnalyser(String symbol)
    {
        symbol = symbol.toUpperCase();

        if (!symbol.matches("[A-Z0-9]+"))
            throw new UnsupportedOperationException("Symbol does not exist (" + symbol + ")");

        this.symbol = symbol;
        this.owningCrypto = false;
        this.priceBought = -1;
        this.trades = 0;
    }

    @Override
    public void run()
    {
        Main.getLogger().log("Analysing...");

        while (true)
        {
            Strategy strategy = new WMA9OverMA50Strategy();
            Trend trend = Main.getTrendDecider().calculateTrend();

            System.out.println("Trend: " + trend + ", BuySignal: " + strategy.calculate());
            sleeping();
        }

        /*
        while (true)
        {
            Trend trend = Main.getTrendDecider().calculateTrend();
            float latestPrice = Main.getCache().getLatestPrice();

            if (trend == Trend.UP && !owningCrypto)
            {
                Main.getLogger().log("Buying BTC for " + latestPrice + " USD");
                priceBought = latestPrice;
                owningCrypto = true;
                ++trades;
            }
            else if (trend == Trend.DOWN && owningCrypto)
            {
                float diff = (latestPrice - priceBought);
                double percentage = (diff / priceBought) * 100;
                profit += percentage - 0.2;
                String percentageFormatted = formatter.format(percentage);

                Main.getLogger().log("Selling BTC for " + latestPrice + "USD (" + percentageFormatted + "% profit)");

                owningCrypto = false;
                ++trades;
                cooldown();
            }
            else
            {
                Main.getLogger().log("No opportunity found...");
            }

            Main.getLogger().log("================================");
            Main.getLogger().log("Trades: " + trades + ", profit: " + profit);
            Main.getLogger().log("================================");

            sleeping();
        }
         */
    }

    private void cooldown()
    {
        try
        {
            sleep(1000 * 60);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void sleeping()
    {
        try
        {
            sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
