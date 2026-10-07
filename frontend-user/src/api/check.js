import request from './request'

export const addCheck = (data) => request.post('/check', data)
export const updateCheck = (id, data) => request.put(`/check/${id}`, data)
export const deleteCheck = (id) => request.delete(`/check/${id}`)
export const pageCheck = (params) => request.get('/check/page', { params })
export const todayCheck = () => request.get('/check/today')
