import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe, DatePipe } from '@angular/common';
import { OrderService } from '../../../core/services/order.service';
import { Order, OrderStatus } from '../../../core/models/order.model';

@Component({
  selector: 'app-order-list',
  imports: [RouterLink, DecimalPipe, DatePipe],
  templateUrl: './order-list.html',
  styleUrl: './order-list.css',
})
export class OrderList implements OnInit {
  private orderService = inject(OrderService);

  orders = signal<Order[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading.set(true);
    this.orderService.getMyOrders().subscribe({
      next: (data) => {
        this.orders.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Impossible de charger vos commandes');
        this.loading.set(false);
        console.error(err);
      }
    });
  }

  statusLabel(status: OrderStatus): string {
    const labels: Record<OrderStatus, string> = {
      PENDING: 'En attente',
      CONFIRMED: 'Confirmée',
      SHIPPED: 'Expédiée',
      DELIVERED: 'Livrée',
      CANCELLED: 'Annulée'
    };
    return labels[status];
  }

  statusColor(status: OrderStatus): string {
    const colors: Record<OrderStatus, string> = {
      PENDING: 'bg-orange-50 text-orange-600',
      CONFIRMED: 'bg-blue-50 text-blue-600',
      SHIPPED: 'bg-purple-50 text-purple-600',
      DELIVERED: 'bg-green-50 text-green-600',
      CANCELLED: 'bg-red-50 text-red-600'
    };
    return colors[status];
  }
}