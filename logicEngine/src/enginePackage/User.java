package enginePackage;

import DTOs.DataToAlertDTO;
import DTOs.UserDataDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import generated.RseItem;

public class User
{
    /******************************************************************************/
    public enum Type {ADMIN, TRADER}
    /******************************************************************************/
    private String name;
    private UserHoldings holdings;
    private List<Action> actionsHistory;
    private int cashMoney = 0;
    private Type type;
    private DataToAlertDTO dataToAlertDTO;
    /******************************************************************************/
    public User(String name, Type type)
    {
        if (type == Type.TRADER)
            holdings = new UserHoldings();
        this.name = name;
        this.actionsHistory = new ArrayList<>();
        this.type = type;

        this.dataToAlertDTO = new DataToAlertDTO();
    }
    /******************************************************************************/
    public String getName() { return name; }
    public UserHoldings getHoldings() { return holdings; }
    public Type getType() { return type; }
    public int getCashMoney() { return this.cashMoney; }
    /******************************************************************************/
    public void updateHoldings(int stockAmount, Command.Way way, Stock stock)
    {
        String date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS").format(new java.util.Date());
        int oldCashMoney = this.cashMoney;

        holdings.updateHoldings(stock.getSymbol(), stockAmount, stock.getCurrValue(), way);

        if (way == Command.Way.BUY)
        {
            this.cashMoney -= (stockAmount * stock.getCurrValue());
            addAction(Action.Type.BUY, date, stockAmount, oldCashMoney, this.cashMoney, stock.getSymbol());
        }
        else
        {
            this.cashMoney += (stockAmount * stock.getCurrValue());
            addAction(Action.Type.SELL, date, stockAmount, oldCashMoney, this.cashMoney, stock.getSymbol());
        }

    }
    /******************************************************************************/
    public void addCashMoney(int cashMoney)
    {
        String date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS").format(new java.util.Date());
        addAction(Action.Type.LOADMONEY, date, cashMoney, this.cashMoney, this.cashMoney + cashMoney, "-");
        this.cashMoney += cashMoney;
    }
    /******************************************************************************/
    public void addStocks(List<RseItem> rseItem, Stocks stocks)
    {
        for (RseItem I : rseItem) {
            holdings.updateHoldings(I.getSymbol(),
                    I.getQuantity(),
                    stocks.findStock(I.getSymbol()).getCurrValue(),
                    Command.Way.BUY
                    );
        }
    }
    /******************************************************************************/
    private void addAction(Action.Type type, String date, int amount,  int currencyBefore, int currencyAfter, String symbol)
    {
        this.actionsHistory.add(new Action(type, date, amount, currencyBefore, currencyAfter, symbol));
    }
    /******************************************************************************/
    public UserDataDTO userToDTO() { return new UserDataDTO(this.cashMoney, this.actionsHistory); }
    /******************************************************************************/
    public int getStockAmount(String chosenStock)
    {
        return holdings.getStockAmount(chosenStock);
    }
    /******************************************************************************/
    public void addTradeAlert(String symbol, int price, int amount, int leftOvers, String oppositeUserName, String way)
    {
        dataToAlertDTO.addAlertData(symbol, price, amount, leftOvers, oppositeUserName, way);
    }
    /******************************************************************************/
    public List<DataToAlertDTO.StockAlert> getDataToAlertDTO()
    {
        if (dataToAlertDTO.isEmpty())
            return null;
        else
        {
            return dataToAlertDTO.getDataList();
        }
    }
    /******************************************************************************/
}
