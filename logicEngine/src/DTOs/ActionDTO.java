package DTOs;

import enginePackage.Action;

public class ActionDTO
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
    public ActionDTO(Action action)
    {
        if (action.getType() == Action.Type.BUY)
            this.type = Type.BUY;
        else if(action.getType() == Action.Type.SELL)
            this.type = Type.SELL;
        else
            this.type = Type.LOADMONEY;

        this.date = action.getDate();
        this.amount = action.getAmount();
        this.currencyBefore = action.getCurrencyBefore();
        this.currencyAfter = action.getCurrencyAfter();
        this.symbol = action.getSymbol();   // if type == LOADMONEY so symbol will send as '-'
    }
    /******************************************************************************/
}
