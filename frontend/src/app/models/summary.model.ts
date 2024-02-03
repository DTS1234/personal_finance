import {Asset} from './asset.model';

export class Summary {

  constructor(
    public id: number,
    public date: string,
    public money: number,
    public currency: string,
    public assets: Asset[]
  ) {
  }

}
