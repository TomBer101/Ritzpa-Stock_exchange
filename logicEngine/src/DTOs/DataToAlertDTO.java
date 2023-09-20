package DTOs;

import java.util.ArrayList;
import java.util.List;

public class DataToAlertDTO
{
    /******************************************************************************/
    private List<StockAlert> dataList;
    /******************************************************************************/
    public class StockAlert implements Cloneable
    {
        private String symbol;
        private int price;
        private int amount;
        private int leftOvers;
        private String oppositeUserName;
        private String way;

        public StockAlert(String symbol, int price, int amount, int leftOvers, String oppositeUserName, String way)
        {
            this.symbol = symbol;
            this.price = price;
            this.amount = amount;
            this.leftOvers = leftOvers;
            this.oppositeUserName = oppositeUserName;
            this.way = way;
        }
    }
    /******************************************************************************/
    public DataToAlertDTO()
    {
        dataList = new ArrayList<>();
    }
    /******************************************************************************/
    public void addAlertData(String symbol, int price, int amount, int leftOvers, String oppositeUserName, String way)
    {
        dataList.add(new StockAlert(symbol, price, amount, leftOvers, oppositeUserName, way));
    }
    /******************************************************************************/
    public List<StockAlert> getDataList()
    {
        List<StockAlert> res = new ArrayList<>(dataList);
        makeEmpty();
        return res;
    }
    /******************************************************************************/
    public void makeEmpty() { dataList.clear(); }
    /******************************************************************************/
    public boolean isEmpty() { return dataList.isEmpty(); }
    /******************************************************************************/
}
