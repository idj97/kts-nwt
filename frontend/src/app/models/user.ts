
export class User  {
  public id: number;
  firstname: string;
  lastname: string;
  username: string;
  password: string;
  email: string;
  token: string;
  authorities: Array<string>;
  banned: boolean;
}
