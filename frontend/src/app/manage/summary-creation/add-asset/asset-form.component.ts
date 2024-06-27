import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Asset} from '../../../models/asset.model';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {SummaryService} from "../../../services/summary.service";
import {Summary} from "../../../models/summary.model";
import {Item} from "../../../models/item.model";
import {StockItemRequestDTO} from "../stock-item-form/stock-item.model";

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
    this.assetForm = this.formBuilder.group({
      assetName: ['', Validators.required],
      money: [{value: '', disabled: false}],
      type: ['', Validators.required],
      items: this.formBuilder.array([])
    });
  }

  ngOnInit(): void {
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
            assetName: this.asset.name,
            money: this.asset.money,
            type: this.asset.type,
          });

          const itemsArray = this.asset.items.map(item => this.createItemGroup(item));
          this.assetForm.setControl('items', this.formBuilder.array(itemsArray));
          this.onChanges();
        }
      });
    })
  }

  get items(): FormArray {
    return this.assetForm.get('items') as FormArray;
  }

  createItemGroup(item?: any): FormGroup {
    if (item) {
      if (item.type === "CUSTOM") {
        return this.formBuilder.group({
          name: [item.name, Validators.required],
          money: [item.money, Validators.required],
          type: [item.type, Validators.required]
        });
      } else {
        return this.formBuilder.group({
          ticker: [item.ticker, Validators.required],
          purchasePrice: [item.purchasePrice, Validators.required],
          currentPrice: [item.currentPrice, Validators.required],
          name: [item.name, Validators.required],
          quantity: [item.quantity, Validators.required],
          type: [item.type, Validators.required]
        });
      }
    } else {
      return this.formBuilder.group({
        name: ['', Validators.required],
        money: ['', Validators.required],
        type: ['CUSTOM', Validators.required]
      });
    }
  }

  onChanges(): void {
    let itemControls = this.assetForm.get('items') as FormArray
    itemControls.valueChanges.subscribe(val => {
      const type = this.asset?.type
      console.log("on changes !")
      if (type == "STOCK") {
        const items: StockItemRequestDTO[] = this.assetForm.get('items').value;
        let sum = 0
        items.forEach(i => sum += (i.currentPrice * i.quantity))
        this.assetForm.patchValue({money: sum}, {emitEvent: false})
        console.log("new sum " + sum)
      }
    });
  }

  onSubmit(): void {
    if (this.assetForm.invalid) {
      console.log("INVALID FORM ...")
      return;
    }

    const assetData = this.assetForm.value;
    const asset = new Asset(
      null,
      assetData.assetName,
      assetData.money,
      assetData.items,
      this.summary.id,
      assetData.type
    );

    const newSummary = JSON.parse(JSON.stringify(this.summary));

    if (this.mode == 'add') {
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

      this.asset.name = assetData.assetName
      this.asset.money = assetData.money;
      this.asset.items = assetData.items;

      const newSummary = JSON.parse(JSON.stringify(this.summary));
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
  }

  updateFormArray(type: string): void {
    if (type === 'STOCK') {
      this.addStockItem();
    } else if (type === 'CUSTOM') {
      this.addItem();
    }
  }

  addItem(): void {
    const newItem = this.formBuilder.group({
      name: ['', Validators.required],
      money: ['', Validators.required],
      type: ['CUSTOM', Validators.required]
    });
    this.items.push(newItem);
  }

  addStockItem(): void {
    const stockItemForm = this.formBuilder.group({
      ticker: ['', Validators.required],
      purchasePrice: ['', Validators.required],
      currentPrice: [{value: '1'}, Validators.required],
      name: ['', Validators.required],
      quantity: ['', Validators.required],
      type: ['STOCK', Validators.required]
    });
    this.items.push(stockItemForm);
  }

  cancelSummary() {

  }
}
