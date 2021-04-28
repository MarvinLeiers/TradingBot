package de.marvinleiers.tradingbot.analyse.indicators;

import de.marvinleiers.tradingbot.Main;

public abstract class Indicator
{
    private final String symbol;

    public Indicator()
    {
        this.symbol = Main.getSymbol();
    }

    public abstract float calculate();
}
