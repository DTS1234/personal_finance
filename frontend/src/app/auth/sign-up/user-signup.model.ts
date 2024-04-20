export class UserSignUpRequest {

  constructor(public email: string,
              public password: string,
              public birthdate: Date | null,
              public gender: string | null,
              public lastname: string | null,
              public firstname: string | null) {
  }


}
