import {Item} from "./item.model";

export class Asset {

  constructor(
    public id: number,
    public name: string,
    public moneyValue: number,
    public items: Item[]
  ) {
  }

}
