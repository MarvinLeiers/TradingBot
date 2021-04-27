package de.marvinleiers.tradingbot.analyse.indicators.movingaverages;

import com.binance.api.client.domain.market.Candlestick;
import de.marvinleiers.tradingbot.Main;

import java.util.List;

public class WeightedMovingAverage extends MovingAverage
{
    public WeightedMovingAverage(String symbol, int lookBack)
    {
        super(symbol, lookBack);
    }

    @Override
    public float calculate()
    {
        List<Candlestick> candleSticks = Main.getCache().getCandlesticks(lookBack);

        float sum = 0;
        int n = candleSticks.size();
        int gaussSum = (n * n + n) / 2;
        int x = 1;

        for (Candlestick candlestick : candleSticks)
        {
            sum += Float.parseFloat(candlestick.getClose()) * (x / (float) gaussSum);
            x++;
        }

        return sum;
    }
}
