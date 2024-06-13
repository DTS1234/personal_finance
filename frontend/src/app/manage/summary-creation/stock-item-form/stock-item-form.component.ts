// stock-item-form.component.ts
import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-stock-item-form',
  templateUrl: './stock-item-form.component.html',
  styleUrls: ['./stock-item-form.component.css'],
  imports: [
    ReactiveFormsModule
  ],
  standalone: true
})
export class StockItemFormComponent {
  @Input() assetForm!: FormGroup;

  constructor() {
  }
}
