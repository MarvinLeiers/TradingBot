package de.marvinleiers.tradingbot.analyse.indicators.movingaverages;

import de.marvinleiers.tradingbot.analyse.indicators.Indicator;

public abstract class MovingAverage extends Indicator
{
    protected int lookBack;
    protected long from;

    public MovingAverage(int lookBack)
    {
        this.lookBack = lookBack;
        this.from = System.currentTimeMillis();
    }

    public MovingAverage(int lookBack, long from)
    {
        this.from = from;
        this.lookBack = lookBack;
    }
}
