import {Item} from './item.model';
import {StockItemRequestDTO} from "../manage/summary-creation/stock-item-form/stock-item.model";

export class Asset {

  constructor(
    public id: string,
    public name: string,
    public money: number,
    public items: (Item|StockItemRequestDTO)[],
    public summaryId: string,
    public type: string
  ) {
  }

}
