import request from './request'

export const pageNotice = (params) => request.get('/notice/page', { params })
export const unreadCount = () => request.get('/notice/unread')
export const readNotice = (id) => request.put(`/notice/${id}/read`)
export const readAllNotice = () => request.put('/notice/read-all')
