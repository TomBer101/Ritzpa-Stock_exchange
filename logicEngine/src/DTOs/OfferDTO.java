package DTOs;

import enginePackage.Command;

public class OfferDTO
{
    /******************************************************************************/
    private String date;
    private String way;
    private int offerValue;
    private int amount;
    private String initiatorUserName;
    private String type;
    /******************************************************************************/
    public OfferDTO(Command cmd)
    {
        this.date = cmd.getDate();
        this.way = cmd.getWay().toString();
        this.offerValue = cmd.getPriceLimit();
        this.amount = cmd.getAmountOfStocks();
        this.initiatorUserName = cmd.getInitiator().getName();
        this.type = cmd.getType().toString();
    }
    /******************************************************************************/
    public String getDate() { return date; }
    public String getWay() { return way; }
    public int getOfferValue() { return offerValue; }
    public int getAmount() { return amount; }
    public String getInitiatorUserName() { return initiatorUserName; }
    public String getType() { return type; }
    /******************************************************************************/
    public void setDate(String date) { this.date = date; }
    public void setWay(String way) { this.way = way; }
    public void setOfferValue(int offerValue) { this.offerValue = offerValue; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setInitiatorUserName(String initiatorUserName) { this.initiatorUserName = initiatorUserName; }
    public void setType(String type) { this.type = type; }
    /******************************************************************************/
}
