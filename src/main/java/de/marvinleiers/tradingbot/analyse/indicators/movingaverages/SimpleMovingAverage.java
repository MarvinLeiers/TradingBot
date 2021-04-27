package de.marvinleiers.tradingbot.analyse.indicators.movingaverages;

import com.binance.api.client.domain.market.Candlestick;
import de.marvinleiers.tradingbot.Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SimpleMovingAverage extends MovingAverage
{
    public SimpleMovingAverage(String symbol, int lookBack)
    {
        super(symbol, lookBack);
    }

    public SimpleMovingAverage(String symbol, int lookBack, long from)
    {
        super(symbol, lookBack, from);
    }

    @Override
    public float calculate()
    {
        List<Candlestick> candlesticks = Main.getCache().getPastCandleSticks(lookBack, from);

        float sum = 0;

        for (Candlestick candlestick : candlesticks)
        {
            sum += Float.parseFloat(candlestick.getClose());
        }

        return sum / candlesticks.size();
    }
}
