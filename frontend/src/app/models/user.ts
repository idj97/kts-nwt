
export class User  {
  public id: number;
  firstname: string;
  lastname: string;
  username: string;
  email: string;
  password: string;


  getEmail() {
    return this.email;
  }
}
