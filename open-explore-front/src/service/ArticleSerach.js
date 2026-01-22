import { FetchPost,FetchGet } from './fetch';

export const  SearchArticle = async (params) => {
  return await FetchPost('/searchApi/article/v1/list',{
    body: JSON.stringify(params)
  })
}
export const  SearchArticleAgg = async (params) => {
  return await FetchPost('/searchApi/article/v1/aggregations',{
    body: JSON.stringify(params)
  })
}

export const  SearchArticleDetail = async (id) => {
  return await FetchGet('/searchApi/article/v1/getOne/' + id)
}