package enginePackage;

public class Item
{
    /******************************************************************************/
    private String symbol;
    private int quantity;
    private int valueOfItem;
    /******************************************************************************/
    public Item(String symbol, int quantity, int valueOfItem)
    {
        this.symbol = symbol;
        this.quantity = quantity;
        this.valueOfItem = valueOfItem;
    }
    /******************************************************************************/
    public int getItemValue() { return valueOfItem; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    /******************************************************************************/
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setItemValue(int valueOfItem) { this.valueOfItem = valueOfItem; }
    /******************************************************************************/
}
