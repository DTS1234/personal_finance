// stock-item-request.dto.ts
export interface StockItemRequestDTO {
  ticker: string;
  purchasePrice: number;
  currentPrice: number;
  name: string;
  quantity: number;
  money: number;
  type: string;
}
