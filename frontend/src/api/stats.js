import request from './request'

export const getDashboard = () => request.get('/stats/dashboard')
export const getOverview = () => request.get('/stats/overview')
