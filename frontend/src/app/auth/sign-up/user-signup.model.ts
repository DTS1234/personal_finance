export class UserSignUpData {

  constructor(public email: string,
              public password: string,
              public birthdate: string | null,
              public gender: string | null,
              public lastname: string | null,
              public firstname: string | null) {
  }
}
