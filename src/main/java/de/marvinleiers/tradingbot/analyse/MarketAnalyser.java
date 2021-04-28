package de.marvinleiers.tradingbot.analyse;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.bots.SimulationBot;
import de.marvinleiers.tradingbot.strategies.BuySignal;
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

        SimulationBot simulationBot = new SimulationBot("apiKey", "secret");

        simulationBot.buy();

        while (true)
        {
            BuySignal strategy = new WMA9OverMA50Strategy().calculate();
            float latestPrice = Main.getCache().getLatestPrice();

            if (strategy == BuySignal.BUY && !owningCrypto)
            {
                Main.getLogger().log("Buying BTC for " + latestPrice);
                owningCrypto = true;
                priceBought = latestPrice;
            }
            else if (strategy == BuySignal.SELL && owningCrypto)
            {
                float diff = (latestPrice - priceBought);
                double percentage = (diff / priceBought) * 100;
                profit += percentage - 0.2;
                String percentageFormatted = formatter.format(percentage);

                Main.getLogger().log("Selling BTC for " + latestPrice + "USD (" + percentageFormatted + "% profit)");
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
