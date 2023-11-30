import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {Asset} from '../../../models/asset.model';
import {SummaryService} from "../../../services/summary.service";
import {Summary} from "../../../models/summary.model";

@Component({
  selector: 'app-edit-asset',
  templateUrl: './edit-asset.component.html',
  styleUrls: ['./edit-asset.component.css']
})
export class EditAssetComponent implements OnInit {

  assetForm: FormGroup;
  asset: Asset;
  summary: Summary;
  index: number;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private summaryService: SummaryService
  ) {
  }

  ngOnInit(): void {

    this.summaryService.getCurrentDraft().subscribe(data => {
      this.summary = data
      this.route.queryParams.subscribe(params => {
        console.log(params)
        this.index = params.index as number;
        this.asset = this.summary.assets[this.index]
        this.initializeForm()
      });
    })
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

  onSubmit(): void {
    const assetData = this.assetForm.value;
    const oldMoneyValue = this.asset.money
    this.asset.name = assetData.name;
    this.asset.money = assetData.money;
    this.asset.items = assetData.items;

    console.log(this.asset);

    const newSummary = JSON.parse(JSON.stringify(this.summary));
    newSummary.money -= oldMoneyValue
    newSummary.assets[this.index] = this.asset
    newSummary.money += this.asset.money
    this.summaryService.updateSummary(newSummary).subscribe(
      s => {
        this.summaryService.setNewSummary(s)
        this.assetForm.reset();
        const navigationExtras: NavigationExtras = {
          queryParams: {reload: true} // Add a query parameter to force reload
        };
        const currentUrl = this.router.url;
        const index = currentUrl.indexOf('/edit-asset');
        const newUrl = currentUrl.substring(0, index);
        this.router.navigate([newUrl], navigationExtras).then(r => console.log(r));
      }
    );
  }

  addItem(): void {
    const items = this.assetForm.get('items') as FormArray;
    const newItem = this.formBuilder.group({
      name: ['', Validators.required],
      money: ['', Validators.required],
      quantity: ['', Validators.required]
    });

    items.push(newItem);
  }
}
