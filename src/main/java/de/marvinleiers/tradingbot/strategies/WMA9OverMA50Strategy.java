package de.marvinleiers.tradingbot.strategies;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.MovingAverage;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.SimpleMovingAverage;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.WeightedMovingAverage;
import de.marvinleiers.tradingbot.analyse.trend.Trend;

/**
 *
 * Buy-signal when WMA9 is higher than MA50, sell-signal when current price is below WMA9
 */
public class WMA9OverMA50Strategy extends Strategy
{
    @Override
    public BuySignal calculate()
    {
        Trend trend = Main.getTrendDecider().calculateTrend();

        if (trend != Trend.UP)
            return BuySignal.SELL;

        float latestPrice = Main.getCache().getLatestPrice();
        float wma9 = new WeightedMovingAverage(9).calculate();
        float ma50 = new SimpleMovingAverage(50).calculate();


        if (latestPrice < wma9)
            return BuySignal.SELL;
        else if (wma9 > ma50)
            return BuySignal.BUY;

        return BuySignal.SELL;
    }
}
