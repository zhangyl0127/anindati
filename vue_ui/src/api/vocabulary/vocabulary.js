import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询词汇分类列表
export function listWords(query) {
  return request({
    url: '/vocabulary/handle/list',
    method: 'get',
    params: query
  })
}
// 查询词汇分类详细
export function getWords(wordsId) {
  return request({
    url: '/vocabulary/handle/' + wordsId,
    method: 'get'
  })
}

// 查询词汇分类列表（排除节点）
export function listWordsExcludeChild(wordsId) {
  return request({
    url: '/vocabulary/handle/list/exclude/' + wordsId,
    method: 'get'
  })
}

// 新增分类
export function addWords(data) {
  return request({
    url: '/vocabulary',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateWords(data) {
  return request({
    url: '/vocabulary',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delWords(wordsId) {
  return request({
    url: '/vocabulary/handle/' + wordsId,
    method: 'delete'
  })
}

// 查询分类下拉树结构
export function treeselect() {
  return request({
    url: '/vocabulary/handle/treeselect',
    method: 'get'
  })
}


//————————————————————————*****************列表页*******************————————————————————————————————————————//
// 查询单词列表
export function listOneWord(query) {
  return request({
    url: '/oneword/build/list',
    method: 'get',
    params: query
  })
}

// 单个单词编号修改编辑
export function getOneWord(oneWordId) {
  return request({
    url: '/oneword/build/'+ praseStrEmpty(oneWordId),
    method: 'get',
  })
}

// 新增单个单词
export function addOneWord(data) {
  return request({
    url: '/oneword/build',
    method: 'post',
    data: data
  })
}

// 修改单个单词
export function updateOneWord(data) {
  return request({
    url: '/oneword/build',
    method: 'put',
    data: data
  })
}

// 删除单个单词
export function delOneWord(oneWordId) {
  return request({
    url: '/oneword/build/' + oneWordId,
    method: 'delete'
  })
}

// 导出单词
export function exportOneWord(query) {
  return request({
    url: '/oneword/build/export',
    method: 'get',
    params: query
  })
}

// 下载单词导入模板
export function importTemplate() {
  return request({
    url: '/oneword/build/importTemplate',
    method: 'get'
  })
}

// 单词状态修改
export function changeOneWordStatus(oneWordId, status) {
  const data = {
    oneWordId,
    status
  }
  return request({
    url: '/oneword/build/changeStatus',
    method: 'put',
    data: data
  })
}
