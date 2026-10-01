export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface OrderRequest {
  items: OrderItemRequest[];
  shippingAddress: string;
}

export interface OrderItemResponse {
  productId: number;
  productName: string;
  quantity: number;
  unitPrice: number;
}

export interface Order {
  id: number;
  userId: number;
  userEmail: string;
  items: OrderItemResponse[];
  total: number;
  status: OrderStatus;
  shippingAddress: string;
  createdAt: string;
}