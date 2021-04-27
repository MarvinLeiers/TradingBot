package de.marvinleiers.tradingbot.analyse.indicators.movingaverages;

import de.marvinleiers.tradingbot.analyse.indicators.Indicator;

public abstract class MovingAverage extends Indicator
{
    protected int lookBack;
    protected long from;

    public MovingAverage(String symbol, int lookBack)
    {
        super(symbol);
        this.lookBack = lookBack;
        this.from = System.currentTimeMillis();
    }

    public MovingAverage(String symbol, int lookBack, long from)
    {
        super(symbol);
        this.from = from;
        this.lookBack = lookBack;
    }
}
