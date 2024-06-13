import {Component, OnInit} from '@angular/core';
import {FormArray, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Asset} from '../../../models/asset.model';
import {ActivatedRoute, Router} from '@angular/router';
import {SummaryService} from "../../../services/summary.service";
import {Summary} from "../../../models/summary.model";

@Component({
  selector: 'app-add-asset',
  templateUrl: './add-asset.component.html',
  styleUrls: ['./add-asset.component.css']
})
export class AddAssetComponent implements OnInit {
  assetForm: UntypedFormGroup;
  asset: Asset;
  mode = 'add';
  summary: Summary;

  constructor(private formBuilder: UntypedFormBuilder,
              private summaryService: SummaryService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.summaryService.getCurrentDraft().subscribe(data => {
      this.summary = data
    })

    this.route.queryParams.subscribe(params => {
      const assetString = params.asset;
      if (assetString) {
        this.asset = JSON.parse(assetString);
        this.initializeForm();
        this.mode = 'edit';
      } else {
        this.assetForm = this.formBuilder.group({
          name: ['', Validators.required],
          money: ['', Validators.required],
          type: ['', Validators.required],
          items: this.formBuilder.array([]) // You can add item fields dynamically
        });
      }
    });

  }

  initializeForm(): void {
    this.assetForm = this.formBuilder.group({
      name: [this.asset.name, Validators.required],
      money: [this.asset.money, Validators.required],
      items: this.formBuilder.array(
        this.asset.items.map(item =>
          this.formBuilder.group({
            name: [item.name, Validators.required],
            money: [item.money, Validators.required],
            quantity: [item.quantity, Validators.required]
          })
        )
      )
    });
  }

  get items(): FormArray {
    return this.assetForm.get('items') as FormArray;
  }

  onSubmit(): void {
    if (this.assetForm.invalid) {
      return;
    }

    const assetData = this.assetForm.value;

    const asset = new Asset(
      null,
      assetData.name,
      assetData.money,
      assetData.items,
      this.summary.id,
      assetData.type
    );

    const newSummary = JSON.parse(JSON.stringify(this.summary));

    this.summaryService.addAsset(asset).subscribe(
      asset => {
        newSummary.assets.push(asset)
        newSummary.money += assetData.money
        this.summaryService.setNewSummary(newSummary)
        this.summary = newSummary

        this.assetForm.reset();

        const currentUrl = this.router.url;
        const index = currentUrl.indexOf('/add-asset');
        const newUrl = currentUrl.substring(0, index);
        this.router.navigate([newUrl]).then(r => console.log(r))
      }
    )
  }

  updateFormArray(type: string): void {
    this.items.clear();
    if (type === 'STOCK') {
      this.addStockItem();
    } else if (type === 'NORMAL') {
      this.addItem();
    }
  }

  addItem(): void {
    const newItem = this.formBuilder.group({
      name: ['', Validators.required],
      money: ['', Validators.required],
      quantity: ['', Validators.required]
    });
    this.items.push(newItem);
  }

  addStockItem(): void {
    const stockItemForm = this.formBuilder.group({
      ticker: ['', Validators.required],
      purchasePrice: ['', Validators.required],
      currentPrice: [{ value: '1' }, Validators.required],
      name: ['', Validators.required],
      quantity: ['', Validators.required],
      type: ['STOCK', Validators.required]
    });
    this.items.push(stockItemForm);
  }

  updateAssetMoneyValue() {
    let totalValue = 0;
    const items = this.assetForm.get('items') as UntypedFormArray;
    items.controls.forEach(control => {
      totalValue += control.value.money || 0;
    });
    this.assetForm.get('money').setValue(totalValue);
  }

  cancelSummary() {

  }
}
