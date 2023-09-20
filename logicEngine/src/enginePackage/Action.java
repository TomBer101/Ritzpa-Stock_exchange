package enginePackage;

public class Action
{
    /******************************************************************************/
    public enum Type {BUY, SELL, LOADMONEY}
    /******************************************************************************/
    private Type type;
    private String date;
    private int amount;
    private int currencyBefore;
    private int currencyAfter;
    private String symbol;
    /******************************************************************************/
    public Action(Type type, String date, int amount, int currencyBefore, int currencyAfter, String symbol)
    {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.currencyBefore = currencyBefore;
        this.currencyAfter = currencyAfter;
        this.symbol = symbol;   // if type == LOADMONEY so symbol will send as '-'
    }
    /******************************************************************************/
    public Type getType() { return type; }
    public String getDate() { return date; }
    public int getAmount() { return amount; }
    public int getCurrencyBefore() { return currencyBefore; }
    public int getCurrencyAfter() { return currencyAfter; }
    public String getSymbol() { return symbol; }
    /******************************************************************************/
    public void setType(Type type) { this.type = type; }
    public void setDate(String date) { this.date = date; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setCurrencyBefore(int currencyBefore) { this.currencyBefore = currencyBefore; }
    public void setCurrencyAfter(int currencyAfter) { this.currencyAfter = currencyAfter; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    /******************************************************************************/
}
