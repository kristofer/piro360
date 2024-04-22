import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IPiro {
  id?: number;
  title?: string;
  description?: string | null;
  s3urltovideo?: string | null;
  created?: dayjs.Dayjs | null;
  owner?: IUser | null;
  tags?: ITag[] | null;
}

export const defaultValue: Readonly<IPiro> = {};
