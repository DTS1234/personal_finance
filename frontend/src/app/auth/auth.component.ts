import {Component} from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-login',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.css']
})
export class AuthComponent {

    error: string | null = null
    isLoginMode = true

    onSwitchMode() {
        this.isLoginMode = !this.isLoginMode
    }
}
