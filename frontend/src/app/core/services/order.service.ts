import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, OrderRequest, OrderStatus } from '../models/order.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private apiUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  create(order: OrderRequest): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, order);
  }

  getMyOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/my`);
  }

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/all`);
  }

  updateStatus(id: number, status: OrderStatus): Observable<Order> {
    return this.http.patch<Order>(`${this.apiUrl}/${id}/status`, null, { params: { status } });
  }
}