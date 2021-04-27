package de.marvinleiers.tradingbot.analyse.indicators;

public abstract class Indicator
{
    private final String symbol;

    public Indicator(String symbol)
    {
        symbol = symbol.toUpperCase();

        if (!symbol.matches("[A-Z0-9]+"))
            throw new UnsupportedOperationException("Symbol does not exist (" + symbol + ")");

        this.symbol = symbol;
    }

    public abstract float calculate();

    public abstract String getName();
}
