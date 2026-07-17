import { FetchPost, FetchGet } from './fetch';

export const SearchFacility = async (params) => {
  return await FetchPost('/searchApi/facility/v1/list', {
    body: JSON.stringify(params)
  })
}

export const SearchFacilityAgg = async (params) => {
  return await FetchPost('/searchApi/facility/v1/aggregations', {
    body: JSON.stringify(params)
  })
}

export const SearchFacilityDetail = async (id) => {
  return await FetchGet('/searchApi/facility/v1/getOne/' + id)
}
