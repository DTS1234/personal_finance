export class User {

  constructor(public username: string, public id: string, private _token: string, public _tokenExpirationDate: Date,
              public firstname: string, public lastname:string, public birthdate: string, public gender:string) {
  }

  get token() {
    if (!this._tokenExpirationDate || new Date() > this._tokenExpirationDate) {
      return null
    }
    return this._token
  }


}
