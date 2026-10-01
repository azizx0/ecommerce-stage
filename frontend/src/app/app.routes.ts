import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/products/product-list/product-list').then(m => m.ProductList)
  },
  {
    path: 'products/:id',
    loadComponent: () => import('./features/products/product-detail/product-detail').then(m => m.ProductDetail)
  },
  {
    path: 'cart',
    loadComponent: () => import('./features/cart/cart/cart').then(m => m.Cart)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register').then(m => m.Register)
  },
  {
    path: 'orders',
    loadComponent: () => import('./features/orders/order-list/order-list').then(m => m.OrderList)
  },
  { path: '**', redirectTo: '' }
];