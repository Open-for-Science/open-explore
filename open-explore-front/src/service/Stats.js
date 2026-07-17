import { FetchPost, FetchGet } from './fetch';

export const getTotals = async () => {
  return await FetchGet('/searchApi/stats/v1/totals')
}
