package de.marvinleiers.tradingbot.bots;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

public abstract class Bot
{
    protected final BinanceApiRestClient client;

    public Bot(String apiKey, String secret)
    {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret);
        this.client = factory.newRestClient();
    }

    public abstract void buy();

    public abstract void sell();
}
