import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {Asset} from '../../../models/asset.model';
import {AssetSharedService} from "../../../services/add-asset.service";
import {AssetService} from "../../../services/asset.service";
import {logger} from "codelyzer/util/logger";
import {SummaryService} from "../../../services/summary.service";

@Component({
  selector: 'app-edit-asset',
  templateUrl: './edit-asset.component.html',
  styleUrls: ['./edit-asset.component.css']
})
export class EditAssetComponent implements OnInit {

  assetForm: FormGroup;
  asset: Asset;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private summaryService: SummaryService
  ) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const assetString = params.asset;
      if (assetString) {
        this.asset = JSON.parse(assetString);
        this.initializeForm();
      } else {
        // Handle case when asset parameter is not provided
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

  // Implement updateAsset() and other necessary methods

  onSubmit(): void {
    const assetData = this.assetForm.value;

    this.asset.name = assetData.name;
    this.asset.moneyValue = assetData.moneyValue;
    this.asset.items = assetData.items;

    console.log(this.asset);

    this.route.paramMap.subscribe(params => {
      console.log(params);
      const summaryIdParam = params.get('id');
      const summaryID = Number(summaryIdParam);
      console.log(summaryID);
      this.summaryService.updateAsset(summaryID, this.asset.id, this.asset).subscribe(
        s => console.log(s)
      );
    });


    // Reset the form
    this.assetForm.reset();

    const navigationExtras: NavigationExtras = {
      queryParams: {reload: true} // Add a query parameter to force reload
    };

    const currentUrl = this.router.url;
    const index = currentUrl.indexOf('/edit-asset');
    const newUrl = currentUrl.substring(0, index);
    this.router.navigate([newUrl], navigationExtras).then(r => console.log(r));

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
