package de.marvinleiers.tradingbot.analyse.indicators;

import com.binance.api.client.domain.market.Candlestick;
import de.marvinleiers.tradingbot.Main;

import java.util.List;

public class SimpleMovingAverage extends Indicator
{
    private final int lookBack;

    public SimpleMovingAverage(String symbol, int lookBack)
    {
        super(symbol);
        this.lookBack = lookBack;
    }

    @Override
    public float calculate()
    {
        List<Candlestick> candlesticks = Main.getCache().getCandlesticks(lookBack);

        float sum = 0;

        for (Candlestick candlestick : candlesticks)
            sum += Float.parseFloat(candlestick.getClose());

        return sum / candlesticks.size();
    }

    @Override
    public String getName()
    {
        return null;
    }
}
