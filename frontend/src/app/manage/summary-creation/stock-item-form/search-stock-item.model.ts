// search-stock-data.ts
export class SearchStockData {
  Code: string;
  Name: string;
  Type: string;
  Country: string;
  Currency: string;
  ISIN: string;
  previousClose: number;
  previousCloseDate: string;  // or Date

  constructor(
    Code: string,
    Name: string,
    Type: string,
    Country: string,
    Currency: string,
    Isin: string,
    previousClose: number,
    previousCloseDate: string // or Date
  ) {
    this.Code = Code;
    this.Name = Name;
    this.Type = Type;
    this.Country = Country;
    this.Currency = Currency;
    this.ISIN = Isin;
    this.previousClose = previousClose;
    this.previousCloseDate = previousCloseDate;
  }
}
