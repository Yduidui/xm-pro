// request.js

import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建一个Axios实例
const request = axios.create({
    baseURL: 'http://localhost:8080', // 替换为你的API基础URL
    timeout: 30000, // 请求超时时间
});

// request拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    return config;
}, error => {
    return Promise.reject(error);
});

// response拦截器
request.interceptors.response.use(response => {
    let res = response.data;
    if (typeof res === 'string') {
        try {
            res = res ? JSON.parse(res) : res;
        } catch (e) {
            console.error('JSON解析错误:', e);
        }
    }
    return res;
}, error => {
    if (error.response) {
        if (error.response.status === 404) {
            ElMessage.error('未找到请求接口');
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常。请查看后端控制台报错');
        } else {
            ElMessage.error('请求失败，请稍后重试');
        }
    } else {
        console.error('请求错误:', error.message);
    }
    return Promise.reject(error);
});

export default request;