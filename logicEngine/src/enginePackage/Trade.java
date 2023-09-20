package enginePackage;

import java.text.SimpleDateFormat;

public class Trade
{
    /******************************************************************************/
    private String date;
    private int stockAmount;
    private int stockPrice;
    private int totalTradeValue;
    private String seller;
    private String buyer;
    private Command.Type type;  // lmt or mkt etc..
    /******************************************************************************/
    public Trade(int stockAmount, int stockPrice, String buyer, String seller, Command.Type type)
    {
        this.date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS").format(new java.util.Date());
        this.stockAmount = stockAmount;
        this.stockPrice = stockPrice;
        this.totalTradeValue = stockAmount * stockPrice;
        this.buyer = buyer;
        this.seller = seller;
        this.type = type;
    }
    /******************************************************************************/
    public String getDate() { return date; }
    public int getStockAmount() { return stockAmount; }
    public int getStockPrice() { return stockPrice; }
    public int getTotalTradeValue() { return totalTradeValue; }
    public String getSeller() { return seller; }
    public String getBuyer() { return buyer; }
    public Command.Type getType() { return type; }
    /******************************************************************************/
}
