package DTOs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommandDTO
{
    /******************************************************************************/
    private List<SubCommandTrade> subCommandTrades;
    private int inWaitingList;
    private int offerStockAmount;
    private String date;
    /******************************************************************************/
    public class SubCommandTrade
    {
        private int amount;
        private int currPrice;
        private int totalWorth;
        private String buyer;
        private String seller;

        public SubCommandTrade(int amount, int price, String seller, String buyer)
        {
            this.amount = amount;
            this.currPrice = price;
            this.seller = seller;
            this.buyer = buyer;
            this.totalWorth = amount * price;
        }

        public int getAmount() { return amount; }
        public double getPrice() { return currPrice; }
        public String getUserSoldName() { return seller; }
        public String getUserBoughtName(){ return buyer; }
    }
    /******************************************************************************/
    public CommandDTO(int StockAmount)
    {
        subCommandTrades = new ArrayList<>();
        offerStockAmount = StockAmount;
        inWaitingList = StockAmount;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS").format(new java.util.Date());
    }
    /******************************************************************************/
    public List<SubCommandTrade> getAllSubTrades() { return subCommandTrades; }
    public int getOfferAmount() { return offerStockAmount; }
    public int getInWaitingList() { return inWaitingList; }
    /******************************************************************************/
    public void addSubTrade(int amount, int price, String seller, String buyer)
    {
        subCommandTrades.add(new SubCommandTrade(amount, price, seller, buyer));
        inWaitingList -= amount;
    }
    /******************************************************************************/


}
