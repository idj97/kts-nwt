
export class User  {
  public id: number;
  firstname: string;
  lastname: string;
  username: string;
  password: string;
  email: string;

  getEmail() {
    return this.email;
  }
}
