package de.marvinleiers.tradingbot.strategies;

public abstract class Strategy
{
    public abstract BuySignal calculate();

    public abstract String getName();
}
