package de.marvinleiers.tradingbot.analyse.indicators.movingaverages;

import de.marvinleiers.tradingbot.analyse.indicators.Indicator;

public abstract class MovingAverage extends Indicator
{
    protected final int lookBack;

    public MovingAverage(String symbol, int lookBack)
    {
        super(symbol);
        this.lookBack = lookBack;
    }
}
