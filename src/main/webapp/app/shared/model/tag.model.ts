import { IUser } from 'app/shared/model/user.model';
import { IPiro } from 'app/shared/model/piro.model';

export interface ITag {
  id?: number;
  title?: string | null;
  description?: string | null;
  owner?: IUser | null;
  piros?: IPiro[] | null;
}

export const defaultValue: Readonly<ITag> = {};
