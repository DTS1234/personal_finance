import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormArray} from '@angular/forms';
import {Asset} from '../../../models/asset.model';
import {AssetSharedService} from '../../../services/add-asset.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-add-asset',
  templateUrl: './add-asset.component.html',
  styleUrls: ['./add-asset.component.css']
})
export class AddAssetComponent implements OnInit {
  assetForm: FormGroup;
  asset: Asset;
  mode = 'add';

  constructor(private formBuilder: FormBuilder, private addAssetService: AssetSharedService, private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      const assetString = params.asset;
      if (assetString) {
        this.asset = JSON.parse(assetString);
        this.initializeForm();
        this.mode = 'edit';
      } else {
        this.assetForm = this.formBuilder.group({
          name: ['', Validators.required],
          moneyValue: ['', Validators.required],
          items: this.formBuilder.array([]) // You can add item fields dynamically
        });
      }
    });

  }

  initializeForm(): void {
    this.assetForm = this.formBuilder.group({
      name: [this.asset.name, Validators.required],
      moneyValue: [this.asset.moneyValue, Validators.required],
      items: this.formBuilder.array(
        this.asset.items.map(item =>
          this.formBuilder.group({
            name: [item.name, Validators.required],
            moneyValue: [item.moneyValue, Validators.required],
            quantity: [item.quantity, Validators.required]
          })
        )
      )
    });
  }

  onSubmit(): void {
    if (this.assetForm.invalid) {
      return;
    }

    const assetData = this.assetForm.value;

    const asset = new Asset(
      null,
      assetData.name,
      assetData.moneyValue,
      assetData.items
    );

    this.addAssetService.addAsset(asset);

    // Reset the form
    this.assetForm.reset();

    const currentUrl = this.router.url;
    const index = currentUrl.indexOf('/add-asset');
    const newUrl = currentUrl.substring(0, index);
    this.router.navigate([newUrl]).then(r => console.log(r));
  }

  addItem(): void {
    const items = this.assetForm.get('items') as FormArray;
    const newItem = this.formBuilder.group({
      name: ['', Validators.required],
      moneyValue: ['', Validators.required],
      quantity: ['', Validators.required]
    });

    items.push(newItem);
  }

}
