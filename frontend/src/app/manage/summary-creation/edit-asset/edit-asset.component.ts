import {Component, OnInit} from '@angular/core';
import {FormArray, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {Asset} from '../../../models/asset.model';
import {SummaryService} from "../../../services/summary.service";
import {Summary} from "../../../models/summary.model";
import {Item} from "../../../models/item.model";

@Component({
  selector: 'app-edit-asset',
  templateUrl: './edit-asset.component.html',
  styleUrls: ['./edit-asset.component.css']
})
export class EditAssetComponent implements OnInit {

  assetForm: UntypedFormGroup;
  asset: Asset;
  summary: Summary;
  index: number;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private summaryService: SummaryService
  ) {
  }

  ngOnInit(): void {

    this.summaryService.getCurrentDraft().subscribe(data => {
      this.summary = data;

      this.route.queryParams.subscribe(params => {
        console.log(params)
        this.index = params.index as number;
        this.asset = this.summary.assets[this.index]
        this.assetForm = this.formBuilder.group({
          name: [this.asset.name, Validators.required],
          money: [this.asset.money, Validators.required],
          type: [this.asset.type],
          items: this.formBuilder.array(
            this.asset.items.map(item => {
                if (this.asset.type == "CUSTOM") {
                  return this.formBuilder.group({
                    name: [item.name],
                    money: [item.money],
                    type: [this.asset.type]
                  })
                } else {
                  if (!(item instanceof Item)) {
                    return this.formBuilder.group({
                      ticker: [item.ticker, Validators.required],
                      purchasePrice: [item.purchasePrice, Validators.required],
                      currentPrice: [item.currentPrice, Validators.required],
                      name: [item.name, Validators.required],
                      quantity: [item.quantity, Validators.required],
                      type: ['STOCK', Validators.required]
                    });
                  }
                }
              }
            )
          )
        });
      });

    })
  }

  get items(): FormArray {
    return this.assetForm.get('items') as FormArray;
  }

  onSubmit(): void {
    const assetData = this.assetForm.value;
    console.log(assetData)
    const oldMoneyValue = this.asset.money
    this.asset.name = assetData.name;
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
          const index = currentUrl.indexOf('/edit-asset');
          const newUrl = currentUrl.substring(0, index);
          this.router.navigate([newUrl], navigationExtras).then(r => console.log(r));
        }
      )

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

  cancelEditing() {

  }
}
