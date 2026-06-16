import { FetchPost,FetchGet } from './fetch';

export const  SearchScienceData = async (params) => {
  return await FetchPost('/searchApi/sciencedata/v1/list',{
    body: JSON.stringify(params)
  })
}
export const  SearchScienceDataAgg = async (params) => {
  return await FetchPost('/searchApi/sciencedata/v1/aggregations',{
    body: JSON.stringify(params)
  })
}

export const  SearchScienceDataDetail = async (id) => {
  return await FetchGet('/searchApi/sciencedata/v1/getOne/' + id)
}
