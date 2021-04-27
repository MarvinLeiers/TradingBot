package de.marvinleiers.tradingbot.analyse.trend;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.SimpleMovingAverage;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.WeightedMovingAverage;

public class TrendDecider
{
    private final String symbol;

    public TrendDecider(String symbol)
    {
        symbol = symbol.toUpperCase();

        if (!symbol.matches("[A-Z0-9]+"))
            throw new UnsupportedOperationException("Symbol does not exist (" + symbol + ")");

        this.symbol = symbol;
    }

    public Trend calculateTrend()
    {
        SimpleMovingAverage simpleMovingAverage = new SimpleMovingAverage(symbol, 25);
        WeightedMovingAverage weightedMovingAverage = new WeightedMovingAverage(symbol, 9);
        float currentPrice = Main.getCache().getLatestPrice();

        if (currentPrice <= weightedMovingAverage.calculate())
            return Trend.DOWN;

        return currentPrice > simpleMovingAverage.calculate() ? Trend.UP : Trend.DOWN;
    }
}
