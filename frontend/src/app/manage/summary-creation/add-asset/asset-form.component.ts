import {Component, OnInit} from '@angular/core';
import {FormArray, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {Asset} from '../../../models/asset.model';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {SummaryService} from "../../../services/summary.service";
import {Summary} from "../../../models/summary.model";
import {Item} from "../../../models/item.model";

@Component({
  selector: 'app-add-asset',
  templateUrl: './asset-form.component.html',
  styleUrls: ['./asset-form.component.css']
})
export class AssetFormComponent implements OnInit {
  assetForm: UntypedFormGroup;
  asset: Asset;
  mode = 'add';
  summary: Summary;
  index: number

  constructor(private formBuilder: UntypedFormBuilder,
              private summaryService: SummaryService,
              private router: Router,
              private route: ActivatedRoute) {
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

          console.log("ASSET " + JSON.stringify(this.asset))

          this.assetForm = this.formBuilder.group({
            name: [this.asset.name],
            money: [this.asset.money],
            type: [this.asset.type],
            items: this.formBuilder.array(
              this.asset.items.map(item => {
                  console.log("Adding custom items...")
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
        } else {
          this.assetForm = this.formBuilder.group({
            name: [''],
            money: ['', Validators.required],
            type: ['', Validators.required],
            items: this.formBuilder.array([]) // You can add item fields dynamically
          });
        }
      });
    })
  }

  get items(): FormArray {
    return this.assetForm.get('items') as FormArray;
  }

  onSubmit(): void {
    if (this.assetForm.invalid) {
      console.log(this.assetForm.controls)
      console.log("INVALID FORM, RETURNING ...")
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
      console.log("ADDED ASSET")
    } else {
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
            const index = currentUrl.indexOf('/add-asset');
            const newUrl = currentUrl.substring(0, index);
            this.router.navigate([newUrl], navigationExtras).then(r => console.log(r));
          }
        )
      console.log("UPDATED ASSET")
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
