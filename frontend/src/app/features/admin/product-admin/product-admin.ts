import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { ProductService } from '../../../core/services/product.service';
import { CategoryService } from '../../../core/services/category.service';
import { Product } from '../../../core/models/product.model';
import { Category } from '../../../core/models/category.model';

@Component({
  selector: 'app-product-admin',
  imports: [ReactiveFormsModule, DecimalPipe],
  templateUrl: './product-admin.html',
  styleUrl: './product-admin.css',
})
export class ProductAdmin implements OnInit {
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  private fb = inject(FormBuilder);

  activeTab = signal<'products' | 'categories'>('products');

  products = signal<Product[]>([]);
  categories = signal<Category[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  editingProductId = signal<number | null>(null);
  editingCategoryId = signal<number | null>(null);

  productForm = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    price: [0, [Validators.required, Validators.min(0)]],
    stock: [0, [Validators.required, Validators.min(0)]],
    imageUrl: [''],
    categoryId: [0, Validators.required]
  });

  categoryForm = this.fb.group({
    name: ['', Validators.required],
    description: ['']
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading.set(true);
    this.categoryService.getAll().subscribe({
      next: (cats) => {
        this.categories.set(cats);
        this.loadProducts();
      },
      error: () => {
        this.error.set('Erreur de chargement');
        this.loading.set(false);
      }
    });
  }

  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur de chargement des produits');
        this.loading.set(false);
      }
    });
  }

  // --- Produits ---

  startEditProduct(product: Product): void {
    this.editingProductId.set(product.id);
    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      stock: product.stock,
      imageUrl: product.imageUrl,
      categoryId: product.categoryId
    });
  }

  cancelEditProduct(): void {
    this.editingProductId.set(null);
    this.productForm.reset({ price: 0, stock: 0, categoryId: 0 });
  }

  submitProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const payload = {
      name: this.productForm.value.name!,
      description: this.productForm.value.description || '',
      price: this.productForm.value.price!,
      stock: this.productForm.value.stock!,
      imageUrl: this.productForm.value.imageUrl || '',
      categoryId: this.productForm.value.categoryId!
    };

    const editingId = this.editingProductId();

    if (editingId) {
      this.productService.update(editingId, payload).subscribe({
        next: () => {
          this.loadProducts();
          this.cancelEditProduct();
        },
        error: (err) => this.error.set(err.error?.message || 'Erreur lors de la modification')
      });
    } else {
      this.productService.create(payload).subscribe({
        next: () => {
          this.loadProducts();
          this.cancelEditProduct();
        },
        error: (err) => this.error.set(err.error?.message || 'Erreur lors de la création')
      });
    }
  }

  deleteProduct(id: number): void {
    if (!confirm('Supprimer ce produit ?')) return;
    this.productService.delete(id).subscribe({
      next: () => this.loadProducts(),
      error: (err) => this.error.set(err.error?.message || 'Erreur lors de la suppression')
    });
  }

  // --- Catégories ---

  startEditCategory(category: Category): void {
    this.editingCategoryId.set(category.id);
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description
    });
  }

  cancelEditCategory(): void {
    this.editingCategoryId.set(null);
    this.categoryForm.reset();
  }

  submitCategory(): void {
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }

    const payload = {
      name: this.categoryForm.value.name!,
      description: this.categoryForm.value.description || ''
    };

    const editingId = this.editingCategoryId();

    if (editingId) {
      this.categoryService.update(editingId, payload).subscribe({
        next: () => {
          this.loadData();
          this.cancelEditCategory();
        },
        error: (err) => this.error.set(err.error?.message || 'Erreur lors de la modification')
      });
    } else {
      this.categoryService.create(payload).subscribe({
        next: () => {
          this.loadData();
          this.cancelEditCategory();
        },
        error: (err) => this.error.set(err.error?.message || 'Erreur lors de la création')
      });
    }
  }

  deleteCategory(id: number): void {
    if (!confirm('Supprimer cette catégorie ?')) return;
    this.categoryService.delete(id).subscribe({
      next: () => this.loadData(),
      error: (err) => this.error.set(err.error?.message || 'Erreur lors de la suppression')
    });
  }

  categoryName(categoryId: number): string {
    return this.categories().find(c => c.id === categoryId)?.name || '—';
  }
}