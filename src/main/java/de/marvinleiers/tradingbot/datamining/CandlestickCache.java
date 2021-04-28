package de.marvinleiers.tradingbot.datamining;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import de.marvinleiers.tradingbot.Main;

import java.text.SimpleDateFormat;
import java.util.*;

public class CandlestickCache extends Thread
{
    private final String symbol;
    private final CandlestickInterval interval;
    private Map<Long, Candlestick> candlesticksCache;
    private boolean ready;

    public CandlestickCache(CandlestickInterval interval)
    {
        this.symbol = Main.getSymbol();
        this.interval = interval;
        this.ready = false;
    }

    public float getLatestPrice()
    {
        return Float.parseFloat(getCandlesticks(0).get(0).getClose());
    }

    public List<Candlestick> getCandlesticks(int lookBack)
    {
        while (candlesticksCache == null || !isReady())
            waiting();

        lookBack = lookBack + 1;

        return new ArrayList<>(candlesticksCache.values()).subList(candlesticksCache.values().size() - lookBack,
                candlesticksCache.values().size());
    }

    public List<Candlestick> getPastCandleSticks(int lookBack, long from)
    {
        while (candlesticksCache == null || !isReady())
            waiting();

        List<Candlestick> candlesticks = getCandlestickTimeFrame(0, from);

        return candlesticks.subList(candlesticks.size() - lookBack, candlesticks.size());
    }

    public List<Candlestick> getCandlestickTimeFrame(long from, long to)
    {
        if (to > System.currentTimeMillis())
            to = System.currentTimeMillis();

        List<Candlestick> candlesticks = new ArrayList<>();

        while (candlesticksCache == null || !isReady())
            waiting();

        for (long time : candlesticksCache.keySet())
        {
            if (time >= from && time <= to)
                candlesticks.add(candlesticksCache.get(time));
        }

        return candlesticks;
    }

    private void waiting()
    {
        try
        {
            sleep(1);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
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
