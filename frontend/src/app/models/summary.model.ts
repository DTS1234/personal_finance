import {Asset} from './asset.model';

export class Summary {

  constructor(
    public id: number,
    public date: string,
    public moneyValue: number,
    public assets: Asset[]
  ) {
  }

}
