import { FetchPost, FetchGet } from './fetch';

export const SearchSoftware = async (params) => {
  return await FetchPost('/searchApi/software/v1/list', {
    body: JSON.stringify(params)
  })
}

export const SearchSoftwareAgg = async (params) => {
  return await FetchPost('/searchApi/software/v1/aggregations', {
    body: JSON.stringify(params)
  })
}

export const SearchSoftwareDetail = async (id) => {
  return await FetchGet('/searchApi/software/v1/getOne/' + id)
}
