export interface StockData {
  date: Date;  // using string to simplify, consider using Date for real applications
  open: number;
  high: number;
  low: number;
  close: number;
  adjusted_close: number;
  volume: number;
}
