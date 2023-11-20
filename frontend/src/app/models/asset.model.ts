import {Item} from './item.model';

export class Asset {

  constructor(
    public id: number,
    public name: string,
    public money: number,
    public items: Item[]
  ) {
  }

}
