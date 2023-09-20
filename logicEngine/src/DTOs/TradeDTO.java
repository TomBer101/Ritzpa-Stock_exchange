package DTOs;

import enginePackage.Trade;

public class TradeDTO
{
    /******************************************************************************/
    private String date;
    private int amount;
    private int value;
    /******************************************************************************/
    public TradeDTO(Trade trade)
    {
        this.date = trade.getDate();
        this.amount = trade.getStockAmount();
        this.value = trade.getStockPrice();
    }
    /******************************************************************************/
    public String getDate() { return date; }
    public int getAmount() { return amount; }
    public int getValue() { return value; }
    /******************************************************************************/
    public void setDate(String date) { this.date = date; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setValue(int value) { this.value = value; }
    /******************************************************************************/
}
