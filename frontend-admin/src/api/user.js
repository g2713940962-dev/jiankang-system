import request from './request'

export const pageUser = (params) => request.get('/user/page', { params })
export const updateUserStatus = (id, status) => request.put(`/user/${id}/status`, null, { params: { status } })
export const resetUserPassword = (id) => request.put(`/user/${id}/reset-password`)
export const deleteUser = (id) => request.delete(`/user/${id}`)
