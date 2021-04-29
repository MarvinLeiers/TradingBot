package de.marvinleiers.tradingbot.strategies.strategies;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.SimpleMovingAverage;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.WeightedMovingAverage;
import de.marvinleiers.tradingbot.strategies.BuySignal;
import de.marvinleiers.tradingbot.strategies.DownTrendStrategy;

/**
 *
 * Buy-signal when price is over WMA9, sell-signal when price touches MA20
 */
public class WMA9CrossUpToMA20Strategy extends DownTrendStrategy
{
    private static float priceBought = -1;
    @Override
    public BuySignal calculate()
    {
        float latestPrice = Main.getCache().getLatestPrice();
        float wma9 = new WeightedMovingAverage(9).calculate();
        float ma20 = new SimpleMovingAverage(20).calculate();

        if (latestPrice >= ma20 || latestPrice >= (priceBought * 1.0025))
        {
            priceBought = -1;
            return BuySignal.SELL;
        }
        else if (latestPrice > wma9)
        {
            priceBought = latestPrice;
            return BuySignal.BUY;
        }

        return BuySignal.SELL;
    }

    @Override
    public String getName()
    {
        return "WMA9CrossUpToMA20Strategy";
    }
}
