package enginePackage;

import java.util.ArrayList;
import java.util.List;

public class UserHoldings
{
    /******************************************************************************/
    private List<Item> itemsList;
    private int totalValueHoldings;
    /******************************************************************************/
    public UserHoldings()
    {
        itemsList = new ArrayList<>();
        totalValueHoldings = 0;
    }
    /******************************************************************************/
    public List<Item> getItemList() { return itemsList; }
    public int getTotalValueHoldings() { return totalValueHoldings; }
    /******************************************************************************/
    public void setItemList(List<Item> itemList) { this.itemsList = itemList; }
    public void setTotalValueHoldings(int totalValueHoldings) { this.totalValueHoldings = totalValueHoldings; }
    /******************************************************************************/
    public void updateHoldings(String symbol, int stockAmount, int newPriceLimit, Command.Way way)
    {
        if (way == Command.Way.SELL)
            stockAmount *= (-1);

        for (Item currItem : itemsList)
        {
            if (currItem.getSymbol().equals(symbol))
            {
                int newQuantity = currItem.getQuantity() + stockAmount;

                currItem.setQuantity(newQuantity);
                currItem.setItemValue(newPriceLimit);

                updateTotalValueHoldings();
                return;
            }
        }

        if (way == Command.Way.BUY)
            itemsList.add(new Item(symbol, stockAmount, newPriceLimit));
    }
    /******************************************************************************/
    private void updateTotalValueHoldings()
    {
        totalValueHoldings = 0;

        for (Item item : itemsList)
            totalValueHoldings += (item.getItemValue() * item.getQuantity());
    }
    /******************************************************************************/
    public void add2Holdings(String symbol, int amountOfStocks, int companyVal)
    {
        itemsList.add(new Item(symbol, amountOfStocks,companyVal/amountOfStocks));
        updateTotalValueHoldings();
    }
    /******************************************************************************/
    public int getStockAmount(String chosenStock)
    {
        for(Item curr : itemsList)
        {
            if(curr.getSymbol().equals(chosenStock))
            {
                return curr.getQuantity();
            }
        }
        return 0;
    }
    /******************************************************************************/
}
