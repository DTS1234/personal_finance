import {Asset} from './asset.model';

export class Summary {

  constructor(
    public id: string,
    public date: string,
    public money: number,
    public currency: string,
    public assets: Asset[]
  ) {
  }

}
