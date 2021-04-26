package de.marvinleiers.tradingbot.datamining;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import de.marvinleiers.tradingbot.Main;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CandlestickCache extends Thread
{
    private final String symbol;
    private final CandlestickInterval interval;
    private Map<Long, Candlestick> candlesticksCache;
    private boolean ready;

    public CandlestickCache(String symbol, CandlestickInterval interval)
    {
        if (!symbol.matches("[A-Z0-9]+"))
            throw new UnsupportedOperationException("Symbol " + symbol + " is not valid");

        this.symbol = symbol;
        this.interval = interval;
        this.ready = false;
    }

    @Override
    public void run()
    {
        Main.getLogger().log("Starting data mining...");
        initializeCandlestickCache(symbol, interval);
        startCandlestickEventStreaming(symbol, interval);
    }

    private void initializeCandlestickCache(String symbol, CandlestickInterval interval)
    {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiRestClient client = factory.newRestClient();
        List<Candlestick> candlestickBars = client.getCandlestickBars(symbol.toUpperCase(), interval);

        this.candlesticksCache = new TreeMap<>();

        for (Candlestick candlestickBar : candlestickBars)
        {
            candlesticksCache.put(candlestickBar.getOpenTime(), candlestickBar);
        }

        this.ready = true;
    }

    private void startCandlestickEventStreaming(String symbol, CandlestickInterval interval)
    {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiWebSocketClient client = factory.newWebSocketClient();

        client.onCandlestickEvent(symbol.toLowerCase(), interval, response -> {
            long openTime = response.getOpenTime();
            Candlestick updateCandlestick = candlesticksCache.get(openTime);
            Date date = new Date(openTime);

            if (updateCandlestick == null)
                updateCandlestick = new Candlestick();

            updateCandlestick.setOpenTime(response.getOpenTime());
            updateCandlestick.setOpen(response.getOpen());
            updateCandlestick.setLow(response.getLow());
            updateCandlestick.setHigh(response.getHigh());
            updateCandlestick.setClose(response.getClose());
            updateCandlestick.setCloseTime(response.getCloseTime());
            updateCandlestick.setVolume(response.getVolume());
            updateCandlestick.setNumberOfTrades(response.getNumberOfTrades());
            updateCandlestick.setQuoteAssetVolume(response.getQuoteAssetVolume());
            updateCandlestick.setTakerBuyQuoteAssetVolume(response.getTakerBuyQuoteAssetVolume());
            updateCandlestick.setTakerBuyBaseAssetVolume(response.getTakerBuyQuoteAssetVolume());

            candlesticksCache.put(openTime, updateCandlestick);
        });
    }

    public boolean isReady()
    {
        return ready;
    }

    public Map<Long, Candlestick> getCandlesticksCache()
    {
        return candlesticksCache;
    }
}
