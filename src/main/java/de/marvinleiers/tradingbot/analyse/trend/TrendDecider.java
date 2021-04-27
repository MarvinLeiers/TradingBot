package de.marvinleiers.tradingbot.analyse.trend;

import de.marvinleiers.tradingbot.Main;
import de.marvinleiers.tradingbot.analyse.indicators.movingaverages.MovingAverage;
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
        SimpleMovingAverage simpleMovingAverageY0 = new SimpleMovingAverage(symbol, 50, System.currentTimeMillis()
                - 1000 * 60 * 60);
        SimpleMovingAverage simpleMovingAverageY1 = new SimpleMovingAverage(symbol, 50);
        WeightedMovingAverage weightedMovingAverageNow = new WeightedMovingAverage(symbol, 9);

        float y0 = simpleMovingAverageY0.calculate();
        float y1 = simpleMovingAverageY1.calculate();
        float slope = (y1 - y0) / 60;
        float currentPrice = Main.getCache().getLatestPrice();

        if (slope <= 0 || currentPrice <= weightedMovingAverageNow.calculate())
            return Trend.DOWN;

        return currentPrice > simpleMovingAverage.calculate() ? Trend.UP : Trend.DOWN;
    }
}
