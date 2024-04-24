export class UserInformationData {

  constructor(public email: string,
              public birthdate: Date | null,
              public gender: string | null,
              public lastname: string | null,
              public firstname: string | null) {
  }

}
