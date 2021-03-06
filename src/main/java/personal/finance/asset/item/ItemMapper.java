package personal.finance.asset.item;

public class ItemMapper {
    public static Item toEntity(ItemDomain item) {

        return Item.builder().quantity(item.getQuantity()).name(item.getName()).moneyValue(item.getMoneyValue()).id(item.getId()).build();

    }
}
