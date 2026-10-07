import request from './request'

export const getGoal = () => request.get('/goal')
export const saveGoal = (data) => request.post('/goal', data)
