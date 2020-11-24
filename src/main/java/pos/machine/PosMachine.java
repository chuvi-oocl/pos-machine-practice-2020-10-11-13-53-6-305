package pos.machine;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<String> distinctBarcodes = getDistinctBarcodes(barcodes);

        return getDisplayReceipt(distinctBarcodes, barcodes);
    }

    private List<String> getDistinctBarcodes(List<String> barcodes) {
        return  barcodes.stream().distinct().collect(Collectors.toList());
    }

    private String getDisplayReceipt(List<String> distinctBarcodes, List<String> barcodes) {
        ItemDataLoader itemDataLoader = new ItemDataLoader();
        List<ItemInfo> allItemInfo = itemDataLoader.loadAllItemInfos();
        String result = "***<store earning no money>Receipt***\n";
        int totalPrice = 0;
        for (String barcode : distinctBarcodes) {
            ItemInfo itemInfo = getItemInfo(barcode, allItemInfo);
            int itemCount = getItemCount(barcode, barcodes);
            result += getItemLine(itemCount, itemInfo);
            totalPrice += getItemSubTotal(itemCount, itemInfo);
        }
        result += "----------------------\n";
        result += "Total: "+totalPrice+" (yuan)\n";
        result += "**********************";
        return result;
    }

    private int getItemSubTotal(int itemCount, ItemInfo itemInfo) {
        return itemCount*itemInfo.getPrice();
    }

    private int getItemCount(String barcode, List<String> barcodes) {
        int count = 0;
        for (String barcodeIt : barcodes) {
            if(barcodeIt == barcode){
                count++;
            }
        }
        return count;
    }

    private String getItemLine(int count, ItemInfo itemInfo) {
        return "Name: "+itemInfo.getName()+", Quantity: "+count+", Unit price: "+itemInfo.getPrice()+" (yuan), Subtotal: "+getItemSubTotal(count, itemInfo) +" (yuan)\n";
    }

    private ItemInfo getItemInfo(String barcode, List<ItemInfo> allItemInfo) {
        ItemInfo result = null;

        for(ItemInfo itemInfo: allItemInfo){
            if(itemInfo.getBarcode() == barcode){
                result = itemInfo;
            }
        }
        return result;
    }

    public static void main(String[] args)  {
        PosMachine posMachine = new PosMachine();
        System.out.println(posMachine.printReceipt(ItemDataLoader.loadBarcodes()));
    }
}
