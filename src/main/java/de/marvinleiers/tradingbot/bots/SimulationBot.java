package de.marvinleiers.tradingbot.bots;

import com.binance.api.client.domain.account.NewOrder;
import de.marvinleiers.tradingbot.Main;

public class SimulationBot extends Bot
{
    public SimulationBot(String apiKey, String secret)
    {
        super(apiKey, secret);
    }

    @Override
    public void buy()
    {
        NewOrder order = NewOrder.marketBuy(Main.getSymbol(), "0.002")
                .newClientOrderId("order-0");

        client.newOrderTest(order);

        Main.getLogger().log("Bought " + Main.getSymbol() + " for " + Main.getCache().getLatestPrice() + " ("
                + order.getNewClientOrderId() + ")");
    }

    @Override
    public void sell()
    {
        //TODO:
    }
}
