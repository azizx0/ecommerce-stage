import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-cart',
  imports: [RouterLink, DecimalPipe, ReactiveFormsModule],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {
  cartService = inject(CartService);
  authService = inject(AuthService);
  private orderService = inject(OrderService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  loading = signal(false);
  error = signal<string | null>(null);
  success = signal(false);

  form = this.fb.group({
    shippingAddress: ['', [Validators.required]]
  });

  increment(productId: number, currentQty: number): void {
    this.cartService.updateQuantity(productId, currentQty + 1);
  }

  decrement(productId: number, currentQty: number): void {
    this.cartService.updateQuantity(productId, currentQty - 1);
  }

  remove(productId: number): void {
    this.cartService.removeFromCart(productId);
  }

  placeOrder(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const items = this.cartService.items().map(item => ({
      productId: item.product.id,
      quantity: item.quantity
    }));

    this.orderService.create({
      items,
      shippingAddress: this.form.value.shippingAddress!
    }).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(true);
        this.cartService.clearCart();
        setTimeout(() => this.router.navigate(['/orders']), 1500);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message || 'Impossible de passer la commande');
        console.error(err);
      }
    });
  }
}