import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-normal-item-form',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './normal-item-form.component.html',
  styleUrl: './normal-item-form.component.css'
})
export class NormalItemFormComponent {

  @Input() assetForm!: FormGroup;

  constructor() {

  }

}
