import request from '../utils/request';

export interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  description?: string;
}

export interface OrderItem {
  productId: number;
  quantity: number;
}

export interface CreateOrderRequest {
  userId: number;
  items: OrderItem[];
}

// 商品相关
export const getProducts = () => request.get<Product[]>('/products');

export const getProduct = (id: number) => request.get<Product>(`/products/${id}`);

// 订单相关
export const createOrder = (data: CreateOrderRequest) => request.post('/orders', data);

export const getUserOrders = (userId: number) => request.get(`/orders/user/${userId}`);

export const getOrderDetail = (orderId: number) => request.get(`/orders/${orderId}/detail`);
