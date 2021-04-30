package de.marvinleiers.tradingbot.analyse;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.trend.Trend;
import de.marvinleiers.tradingbot.strategies.BuySignal;
import de.marvinleiers.tradingbot.strategies.Strategy;
import de.marvinleiers.tradingbot.strategies.strategies.WMA9CrossUpToMA20Strategy;
import de.marvinleiers.tradingbot.strategies.strategies.WMA9OverMA50Strategy;

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
        Strategy strategy = null;
        Strategy lastStrategy = null;

        while (true)
        {
            Trend trend = Main.getTrendDecider().calculateTrend();

            if (trend == Trend.UP)
            {
                strategy = new WMA9OverMA50Strategy();
            }
            else
            {
                strategy = new WMA9CrossUpToMA20Strategy();
            }

            if (lastStrategy == null || !lastStrategy.getName().equals(strategy.getName()))
            {
                lastStrategy = strategy;

                Main.getLogger().log("Now using strategy " + strategy.getName());
            }

            BuySignal signal = strategy.calculate();
            float latestPrice = Main.getCache().getLatestPrice();

            if (signal == BuySignal.BUY && !owningCrypto)
            {
                Main.getLogger().log("Buying BTC for " + latestPrice);
                owningCrypto = true;
                priceBought = latestPrice;
            }
            else if (signal == BuySignal.SELL && owningCrypto)
            {
                float diff = (latestPrice - priceBought);
                double percentage = (diff / priceBought) * 100;
                profit += percentage;
                String percentageFormatted = formatter.format(percentage - 0.2);

                Main.getLogger().log("Selling BTC for " + latestPrice + "USD (" + profit + "% profit, " + percentageFormatted + "%)");
                owningCrypto = false;

                cooldown();
            }

            sleeping();
        }
    }

    private void cooldown()
    {
        Main.getLogger().log("Cooldown...");
        try
        {
            sleep(1000 * 60 * 5);
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
