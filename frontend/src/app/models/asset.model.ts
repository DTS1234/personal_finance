import {CustomItem} from './item.model';
import {StockItem} from "../manage/summary-creation/stock-item-form/stock-item.model";

export class Asset {

  constructor(
    public id: string,
    public name: string,
    public money: number,
    public items: (CustomItem|StockItem)[],
    public summaryId: string,
    public type: string
  ) {
  }

}
