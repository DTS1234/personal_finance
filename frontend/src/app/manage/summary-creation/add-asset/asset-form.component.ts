import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Asset} from '../../../models/asset.model';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {SummaryService} from "../../../services/summary.service";
import {Summary} from "../../../models/summary.model";
import {StockItem} from "../stock-item-form/stock-item.model";

@Component({
  selector: 'app-add-asset',
  templateUrl: './asset-form.component.html',
  styleUrls: ['./asset-form.component.css']
})
export class AssetFormComponent implements OnInit {
  assetForm: FormGroup;
  asset: Asset;
  mode = 'add';
  summary: Summary;
  index: number

  constructor(
    private formBuilder: FormBuilder,
    private summaryService: SummaryService,
    private router: Router,
    private route: ActivatedRoute
  ) {

  }

  ngOnInit(): void {
    this.assetForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      money: [0, [Validators.required, Validators.min(0)]],
      type: ['', [Validators.required]],
      items: this.formBuilder.array([])
    });

    this.assetForm.get('type').valueChanges.subscribe((type) => {
      this.clearItems(); // Clear existing items when type changes
      if (type === 'STOCK') {
        this.addStockItem();
      } else if (type === 'CUSTOM') {
        this.addCustomItem();
      }
    });

    this.summaryService.getCurrentDraft().subscribe(data => {
      this.summary = data

      this.route.queryParams.subscribe(params => {
        const assetString = params.asset;
        this.index = params.index as number;

        if (assetString || this.index) {
          if (assetString) { // if asset as a query param, we are adding
            this.asset = JSON.parse(assetString)
            this.mode = "add"
          } else { // if index passed we are editing
            this.asset = this.summary.assets[this.index]
            this.mode = "edit"
          }

          this.assetForm.patchValue({
            name: this.asset.name,
            money: this.asset.money,
            type: this.asset.type,
            items: this.asset.items
          });
        }
      });
    })
  }

  addStockItem() {
    const stockItemGroup = this.formBuilder.group({
      ticker: ['', [Validators.required]],
      purchasePrice: [0, [Validators.required, Validators.min(0)]],
      currentPrice: [0, [Validators.required, Validators.min(0)]],
      name: ['', [Validators.required]],
      quantity: [0, [Validators.required, Validators.min(0)]],
      money: [0, [Validators.required, Validators.min(0)]],
      type: ['STOCK'] // Set a hidden control for type
    });
    this.items.push(stockItemGroup);
  }

  private clearItems() {
    this.items.clear();
  }

  addCustomItem() {
    const customItemGroup = this.formBuilder.group({
      name: ['', [Validators.required]],
      money: [0, [Validators.required, Validators.min(0)]],
      type: ['CUSTOM'] // Set a hidden control for type
    });
    this.items.push(customItemGroup);
  }

  get items(): FormArray {
    return this.assetForm.get('items') as FormArray;
  }

  removeItem(index: number): void {
    const items = this.assetForm.get('items') as FormArray;
    items.removeAt(index);
  }

  onSubmit(): void {

    if (this.assetForm.valid) {
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

      if (this.mode == 'add') {
        console.log("asset to be added: " + JSON.stringify(asset))
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
      } else {
        const oldMoneyValue = this.asset.money

        console.log("updating with : " + JSON.stringify(assetData))

        this.asset.name = assetData.name
        this.asset.money = assetData.money;
        this.asset.items = assetData.items;

        const newSummary = JSON.parse(JSON.stringify(this.summary));
        console.log("asset to be edited: " + JSON.stringify(asset))
        this.summaryService.updateAsset(this.summary.id, this.asset.id, this.asset)
          .subscribe(
            a => {
              newSummary.money -= oldMoneyValue
              newSummary.assets[this.index] = a
              newSummary.money += a.money
              this.summaryService.setNewSummary(newSummary)
              this.summary = newSummary;
              const navigationExtras: NavigationExtras = {
                queryParams: {reload: true} // Add a query parameter to force reload
              };
              const currentUrl = this.router.url;
              const index = currentUrl.indexOf('/add-asset');
              const newUrl = currentUrl.substring(0, index);
              this.router.navigate([newUrl], navigationExtras).then(r => console.log(r));
            }
          )
      }
    } else {
      this.assetForm.markAllAsTouched();
    }
  }

  updateFormArray(type: string): void {
    if (type === 'STOCK') {
      this.addStockItem();
    } else if (type === 'CUSTOM') {
      this.addCustomItem();
    }
  }

  cancelSummary() {

  }
}
