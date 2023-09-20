package enginePackage;

import java.text.SimpleDateFormat;

public class Command
{
    /******************************************************************************/
    public enum Way { BUY, SELL}
    public enum Type { LMT, MKT, FOK, IOC}
    /******************************************************************************/
    private Stock stock;
    private User initiator;
    private Type type; // type of command
    private Way way;    // buy or sell
    private int amountOfStocks;
    private int priceLimit;
    private String date;
    /******************************************************************************/
    public Command(Stock stock, User initiator, Type type, Way way, int amountOfStocks, int priceLimit)
    {
        this.stock = stock;
        this.initiator = initiator;
        this.type = type;
        this.way = way;
        this.amountOfStocks = amountOfStocks;
        this.priceLimit = priceLimit;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS").format(new java.util.Date());
    }
    /******************************************************************************/
    public Stock getStock() { return stock; }
    public User getInitiator() { return initiator; }
    public Type getType() { return type; }
    public Way getWay() { return way; }
    public int getAmountOfStocks() { return amountOfStocks; }
    public int getPriceLimit() { return priceLimit; }
    public String getDate() { return date; }
    /******************************************************************************/
    public void setPriceLimit(int priceLimit) { this.priceLimit = priceLimit; }
    public void substractAmount(int amount) { this.amountOfStocks -= amount;}
    public void setAmount(int newAmount) { this.amountOfStocks = newAmount; }
    /******************************************************************************/
}
