import { Injectable, signal, computed } from '@angular/core';
import { Product } from '../models/product.model';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  items = signal<CartItem[]>([]);

  totalItems = computed(() =>
    this.items().reduce((sum, item) => sum + item.quantity, 0)
  );

  totalPrice = computed(() =>
    this.items().reduce((sum, item) => sum + item.product.price * item.quantity, 0)
  );

  addToCart(product: Product, quantity: number = 1): void {
    const current = this.items();
    const existing = current.find(i => i.product.id === product.id);

    if (existing) {
      this.items.set(current.map(i =>
        i.product.id === product.id ? { ...i, quantity: i.quantity + quantity } : i
      ));
    } else {
      this.items.set([...current, { product, quantity }]);
    }
  }

  updateQuantity(productId: number, quantity: number): void {
    if (quantity <= 0) {
      this.removeFromCart(productId);
      return;
    }
    this.items.set(
      this.items().map(i => i.product.id === productId ? { ...i, quantity } : i)
    );
  }

  removeFromCart(productId: number): void {
    this.items.set(this.items().filter(i => i.product.id !== productId));
  }

  clearCart(): void {
    this.items.set([]);
  }
}