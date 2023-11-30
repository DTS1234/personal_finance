import {Component, OnInit} from '@angular/core';
import {UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
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
  private updatingSummary = false;

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

  onSubmit(): void {
    if (this.assetForm.invalid) {
      return;
    }

    const assetData = this.assetForm.value;

    const asset = new Asset(
      null,
      assetData.name,
      assetData.money,
      assetData.items
    );

    const newSummary = JSON.parse(JSON.stringify(this.summary));

    newSummary.assets.push(asset)
    newSummary.money += assetData.money

    this.summaryService.updateSummary(newSummary).subscribe(
      updatedData => {
        this.summaryService.setNewSummary(updatedData)
        this.summary = updatedData
        this.updatingSummary = false;

        this.assetForm.reset();

        const currentUrl = this.router.url;
        const index = currentUrl.indexOf('/add-asset');
        const newUrl = currentUrl.substring(0, index);
        this.router.navigate([newUrl]).then(r => console.log(r));
      }
    )
  }

  addItem(): void {
    const items = this.assetForm.get('items') as UntypedFormArray;
    const newItem = this.formBuilder.group({
      name: ['', Validators.required],
      money: ['', Validators.required],
      quantity: ['', Validators.required]
    });
    items.push(newItem);
  }

  updateAssetMoneyValue() {
    let totalValue = 0;
    const items = this.assetForm.get('items') as UntypedFormArray;
    items.controls.forEach(control => {
      totalValue += control.value.money || 0;
    });
    this.assetForm.get('money').setValue(totalValue);
  }
}
